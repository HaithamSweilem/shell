package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CmdLs extends AbstractCommand {

    enum FILE_SORTING {
        DIRS_THEN_FILES,
        FILES_THEN_DIRS,
        DATE_CREATED,
        DATE_MODIFIED,
        SIZE,
    }

    public static final String[] HUMAN_READABLE_FILE_SIZES = {
            "B", "KB", "MB", "GB", "TB", "PB"
    };

    private FILE_SORTING orderByOption = FILE_SORTING.DIRS_THEN_FILES;

    public CmdLs() {
        super("ls");
    }

    @Override
    public int validateArgs() {
        return 0;
    }

    @Override
    public int doIt(Shell shell, List<String> commandLineArguments, Environment environment) {
        args.clear();
        orderByOption = FILE_SORTING.DIRS_THEN_FILES;

        if (commandLineArguments != null) {
            args.addAll(commandLineArguments);
        }

        if (args.remove("-sort=files")) {
            orderByOption = FILE_SORTING.FILES_THEN_DIRS;
        }
        if (args.remove("-sort=created")) {
            orderByOption = FILE_SORTING.DATE_CREATED;
        }
        if (args.remove("-sort=modified")) {
            orderByOption = FILE_SORTING.DATE_MODIFIED;
        }
        if (args.remove("-sort=size")) {
            orderByOption = FILE_SORTING.SIZE;
        }


        // no args given, or a dot (.) is given, stay in current dir
        if (args.size() == 0) {
            args.add(environment.getCwd());
        }

        for (String arg : args) {

            if (arg == null || arg.isEmpty()) {
                continue;
            }

            var listing = getListing(arg);
            if (listing != null) {
                System.out.println(listing);
            }
        }

        return 0;
    }

    public String getListing(String fileOrDirName) {
        var f = new File(fileOrDirName);

        if (f.exists()) {

            if (!f.isDirectory()) {

                return getFileAttributesLine(fileOrDirName, f);

            } else if (f.isDirectory() && f.canExecute()) {

                System.out.println("\nContents of: " + fileOrDirName + "\n");

                var dirList = f.listFiles();

                if (dirList != null) {
                    return Arrays.stream(dirList)
                            .sorted(getSortingStrategy())
                            .map(entry -> getFileAttributesLine(fileOrDirName, entry))
                            .collect(Collectors.joining());
                }
            }
        }

        return null;
    }

    public String getFileAttributesLine(String fileOrDirName, File fileObj) {
        try {
            var file = Path.of(fileObj.getAbsolutePath());
//                    var attrs = Files.getFileAttributeView(file, PosixFileAttributeView.class).readAttributes();
//                    var listing = String.format("%s    %s    %s    %5s        %s%n",
//                            PosixFilePermissions.toString(attrs.permissions()),
//                            attrs.owner().getName(),
//                            attrs.group().getName(),
//                            attrs.isDirectory() ? "<DIR>" : "FILE",
//                            fileOrDirName);

            var attrs = Files.getFileAttributeView(file, BasicFileAttributeView.class).readAttributes();
            var fileSize = getReadableFileSize(attrs.size());

            return String.format("%s    %s    %5s    %6s %2s    %s%n",
                    getReadableFileTime(attrs.creationTime().toMillis()),
                    getReadableFileTime(attrs.lastModifiedTime().toMillis()),
                    attrs.isDirectory() ? "<DIR>" : "     ",
                    fileSize[0],
                    fileSize[1],
                    fileObj.getName());

//                    System.out.println(listing);
//                        Map<String, Object> attributes = Files.readAttributes(Path.of(dir), "*", LinkOption.NOFOLLOW_LINKS);
//                        System.out.println();

        } catch (IOException e) {
            return "Error reading file attributes: " + fileOrDirName;
        }
    }

    public FILE_SORTING getOrderByOption() {
        return orderByOption;
    }

    private Comparator<? super File> getSortingStrategy() {
        FILE_SORTING orderBy = getOrderByOption();

        return switch (orderBy) {
            case DIRS_THEN_FILES -> new FileSortByName(true);
            case FILES_THEN_DIRS -> new FileSortByName(false);
            case SIZE -> new FileSortBySize();
            case DATE_MODIFIED -> new FileSortByDateModified();
            case DATE_CREATED -> new FileSortByDateCreated();
        };
    }

    public String getReadableFileTime(final long timeInMillis) {
        var zoneOffset = OffsetDateTime.now().getOffset();
        return LocalDateTime.ofEpochSecond(timeInMillis / 1000, 0, zoneOffset)
                .format(DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss"));
    }

    public String[] getReadableFileSize(final long sizeInBytes) {

        double div = sizeInBytes;
        int sizeNameIdx = 0;

        while (div / 1024 > 1) {
            div = div / 1024;
            sizeNameIdx++;
        }

        var numStr = new DecimalFormat("#,###.##").format(div);
        return new String[] {numStr, CmdLs.HUMAN_READABLE_FILE_SIZES[sizeNameIdx]};
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdLs.class.getSimpleName();
    }
}

class FileSortByDateCreated implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if (o1.isDirectory() && !o2.isDirectory()) {
            return 1;
        }
        if (o2.isDirectory() && !o1.isDirectory()) {
            return -1;
        }

        try {

            var time1 = Files.getFileAttributeView(Path.of(o1.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().creationTime();
            var time2 = Files.getFileAttributeView(Path.of(o2.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().creationTime();
            return time1.compareTo(time2);

        } catch (IOException e) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}

class FileSortByDateModified implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if (o1.isDirectory() && !o2.isDirectory()) {
            return 1;
        }
        if (o2.isDirectory() && !o1.isDirectory()) {
            return -1;
        }

        try {

            var time1 = Files.getFileAttributeView(Path.of(o1.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().lastModifiedTime();
            var time2 = Files.getFileAttributeView(Path.of(o2.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().lastModifiedTime();
            return time1.compareTo(time2);

        } catch (IOException e) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}

class FileSortByName implements Comparator<File> {

    private final boolean dirsFirst;

    public FileSortByName(boolean dirsFirst) {
        this.dirsFirst = dirsFirst;
    }

    @Override
    public int compare(File o1, File o2) {
        if (o1.isDirectory() && !o2.isDirectory()) {
            return dirsFirst ? -1 : 1;
        }
        if (o2.isDirectory() && !o1.isDirectory()) {
            return dirsFirst ? 1 : -1;
        }
        return (o1.getName().compareTo(o2.getName()));
    }
}

class FileSortBySize implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if (o1.isDirectory() && !o2.isDirectory()) {
            return 1;
        }
        if (o2.isDirectory() && !o1.isDirectory()) {
            return -1;
        }

        try {

            var size1 = Files.getFileAttributeView(Path.of(o1.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().size();
            var size2 = Files.getFileAttributeView(Path.of(o2.getAbsolutePath()), BasicFileAttributeView.class).readAttributes().size();
            return Long.compare(size1, size2);

        } catch (IOException e) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
