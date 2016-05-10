package com.qa.framework.library.base;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic IO Utility based on Apache Commons-IO library
 */
public class IOHelper {

    private final static Logger logger = Logger.getLogger(IOHelper.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args)
            throws Exception {
        logger.info(getSourceFromUrl("http://192.168.2.228/build/Biz_English/Android/16-03-29-09-14/"));
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param strDirPath the str dir path
     */
    public static void cleanDirectory(String strDirPath) {
        File dir = new File(strDirPath);
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Compares the contents of two files to determine if they are equal or not.
     *
     * @param strSourcePath the str source path
     * @param strDirPath    the str dir path
     * @return boolean boolean
     */
    public static boolean contentEquals(String strSourcePath, String strDirPath) {
        File Source = new File(strSourcePath);
        File Dir = new File(strDirPath);
        try {
            return FileUtils.contentEquals(Source, Dir);
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return false;
    }

    /**
     * Copies a file to a new location.
     *
     * @param strSrcFilePath  the str src file path
     * @param strDestFilePath the str dest file path
     */
    public static void copyFile(String strSrcFilePath, String strDestFilePath) {
        File src = new File(strSrcFilePath);
        File dest = new File(strDestFilePath);
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Copies a file to a new location.
     *
     * @param src  the src
     * @param dest the dest
     */
    public static void copyFile(File src, File dest) {
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Copies a file to a directory
     *
     * @param strSrcFilePath the str src file path
     * @param strDestDirPath the str dest dir path
     */
    public static void copyFileToDirectory(String strSrcFilePath,
                                           String strDestDirPath) {
        File src = new File(strSrcFilePath);
        File dest = new File(strDestDirPath);
        try {
            FileUtils.copyFileToDirectory(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Copies a whole directory to a new location
     *
     * @param strSrcDirPath  the str src dir path
     * @param strDestDirPath the str dest dir path
     */
    public static void copyDirectory(String strSrcDirPath, String strDestDirPath) {
        File src = new File(strSrcDirPath);
        File dest = new File(strDestDirPath);
        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Copies a whole directory to within another directory
     *
     * @param strSrcDirPath  the str src dir path
     * @param strDestDirPath the str dest dir path
     */
    public static void copyDirectoryUnderDirectory(String strSrcDirPath,
                                                   String strDestDirPath) {
        File src = new File(strSrcDirPath);
        File dest = new File(strDestDirPath);
        try {
            FileUtils.copyDirectoryToDirectory(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Convert from a URL to a String
     *
     * @param url the url
     * @return source from url
     */
    public static String getSourceFromUrl(String url) {
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            return IOUtils.toString(in);
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            IOUtils.closeQuietly(in);
        }
        return url;
    }

    /**
     * Copies bytes from the URL source to a file destination.
     *
     * @param strURL      the str url
     * @param strFilePath the str file path
     */
    public static void downSourceFromUrl(String strURL, String strFilePath) {

        try {
            URL url = new URL(strURL);
            File file = new File(strFilePath);
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            logger.error(e.toString());
        }

    }

    /**
     * Download URL file to local file
     *
     * @param url              the url
     * @param strLocalFilePath the str local file path
     */
    public static void downFileFromUrl(String url, String strLocalFilePath) {

        InputStream in = null;
        try {
            in = new URL(url).openStream();
            byte[] ByteArray = IOUtils.toByteArray(in);
            FileUtils.writeByteArrayToFile(new File(strLocalFilePath),
                    ByteArray);
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            IOUtils.closeQuietly(in);
        }

    }

    /**
     * Deletes a directory recursively.
     *
     * @param strDirPath the str dir path
     */
    public static void deleteDirectory(String strDirPath) {
        File dir = new File(strDirPath);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Deletes a file
     *
     * @param strFilePath the str file path
     */
    public static void deleteFile(String strFilePath) {
        File file = new File(strFilePath);
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return temp directory
     */
    public static String getTempDirectory() {
        return FileUtils.getTempDirectoryPath();
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return user directory
     */
    public static String getUserDirectory() {
        return FileUtils.getUserDirectoryPath();
    }

    /**
     * Creat file.
     *
     * @param filePath the file path
     */
    public static void creatFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories.
     *
     * @param strDir the str dir
     */
    public static void createNestDirectory(String strDir) {
        File Dir = new File(strDir);
        try {
            FileUtils.forceMkdir(Dir);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * FilenameUtils defines six components within a filename (example
     * C:\dev\project\file.txt): the prefix - C:\ the path - dev\project\ the
     * full path - C:\dev\project\ the name - file.txt the base name - file the
     * extension - txt
     */

    /**
     * Gets the base name, minus the full path and extension, from a full
     * filename. C:\dev\project\file.txt - file
     *
     * @param strFileName the str file name
     * @return base name
     */
    public static String getBaseName(String strFileName) {
        return FilenameUtils.getBaseName(strFileName);
    }

    /**
     * Gets the extension of a filename. C:\dev\project\file.txt - txt
     *
     * @param strFileName the str file name
     * @return extension extension
     */
    public static String getExtension(String strFileName) {
        return FilenameUtils.getExtension(strFileName);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path.
     * C:\dev\project\file.txt - C:\dev\project\
     *
     * @param strFileName the str file name
     * @return full path
     */
    public static String getFullPath(String strFileName) {
        return FilenameUtils.getFullPath(strFileName);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path, and
     * also excluding the final directory separator. C:\dev\project\file.txt -
     * C:\dev\project
     *
     * @param strFileName the str file name
     * @return full path no end separator
     */
    public static String getFullPathNoEndSeparator(String strFileName) {
        return FilenameUtils.getFullPathNoEndSeparator(strFileName);
    }

    /**
     * Gets the name minus the path from a full filename.
     * C:\dev\project\file.txt - file.txt
     *
     * @param strFileName the str file name
     * @return name name
     */
    public static String getName(String strFileName) {
        return FilenameUtils.getName(strFileName);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix.
     * C:\dev\project\file.txt - dev\project\
     *
     * @param strFileName the str file name
     * @return path path
     */
    public static String getPath(String strFileName) {
        return FilenameUtils.getPath(strFileName);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix, and also
     * excluding the final directory separator. C:\dev\project\file.txt - dev\project
     *
     * @param strFileName the str file name
     * @return path no end separator
     */
    public static String getPathNoEndSeparator(String strFileName) {
        return FilenameUtils.getPathNoEndSeparator(strFileName);
    }

    /**
     * Gets the prefix from a full filename C:\dev\project\file.txt - C:\
     *
     * @param strFileName the str file name
     * @return prefix prefix
     */
    public static String getPrefix(String strFileName) {
        return FilenameUtils.getPrefix(strFileName);
    }

    /**
     * Checks a filename to see if it matches the specified wildcard matcher,
     * always testing case-sensitive.
     *
     * @param filename        - the filename to match on
     * @param wildcardMatcher - the wildcard string to match against
     * @param caseSensitivity - what case sensitivity rule to use, null means case-sensitive
     * @return boolean boolean
     */
    public static boolean wildcardMatch(String filename,
                                        String wildcardMatcher, IOCase caseSensitivity) {
        return FilenameUtils.wildcardMatch(filename, wildcardMatcher,
                caseSensitivity);
    }

    /**
     * Checks a filename to see if it matches the specified wildcard matcher,
     * always testing case-sensitive.
     *
     * @param filename        - the filename to match on
     * @param wildcardMatcher - the wildcard string to match against
     * @return boolean boolean
     */
    public static boolean wildcardMatch(String filename, String wildcardMatcher) {
        return FilenameUtils.wildcardMatch(filename, wildcardMatcher);
    }

    /**
     * Get the size of the specified file
     *
     * @param strfilepath the strfilepath
     * @return size of file
     */
    public static long getSizeOfFile(String strfilepath) {
        File destFile = new File(strfilepath);
        return FileUtils.sizeOf(destFile);
    }

    /**
     * Counts the size of a directory recursively (sum of the length of all
     * files).
     *
     * @param strdirpath the strdirpath
     * @return size of directory
     */
    public static long getSizeOfDirectory(String strdirpath) {
        File destDir = new File(strdirpath);
        return FileUtils.sizeOfDirectory(destDir);
    }

    /**
     * Get the free space on a drive
     *
     * @param strDrivePath the str drive path
     * @return free spacethe io exception
     * @throws IOException the io exception
     */
    public static long getFreeSpace(String strDrivePath) throws IOException {
        return FileSystemUtils.freeSpaceKb(strDrivePath);
    }

    /**
     * Returns the disk size of the volume which holds the working directory.
     *
     * @return free space
     * @throws IOException the io exception
     */
    public static long getFreeSpace() throws IOException {
        return FileSystemUtils.freeSpaceKb();
    }

    /**
     * Finds files within a given directory.
     *
     * @param strdirpath the strdirpath
     * @return list list
     */
    public static List<String> listFilesInDirectory(String strdirpath) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory, null,
                false);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds files within a given directory. All files found are filtered by an
     * IOFileFilter.
     *
     * @param strdirpath         the strdirpath
     * @param WildcardFileFilter filter to apply when finding files. such as "*.*"
     * @return list list
     */
    public static List<String> listFilesInDirectory(String strdirpath,
                                                    String WildcardFileFilter) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                new WildcardFileFilter(WildcardFileFilter), null);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds files within a given directory which match an array of extensions.
     *
     * @param strdirpath the strdirpath
     * @param extensions an array of extensions.such as new String[]{"java","xml"}
     * @return list list
     */
    public static List<String> listFilesInDirectory(String strdirpath,
                                                    String[] extensions) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                extensions, false);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds files within a given directory (and its subdirectories).
     *
     * @param strdirpath the strdirpath
     * @return list list
     */
    public static List<String> listFilesInDirectoryRecursive(String strdirpath) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory, null,
                true);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds files within a given directory (and its all subdirectories). All
     * files found are filtered by an IOFileFilter.
     *
     * @param strdirpath         the strdirpath
     * @param WildcardFileFilter filter to apply when finding files. such as "*.*"
     * @return list list
     */
    public static List<String> listFilesInDirectoryRecursive(String strdirpath,
                                                             String WildcardFileFilter) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                new WildcardFileFilter(WildcardFileFilter),
                TrueFileFilter.INSTANCE);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds file names within a given directory (and its all subdirectories).
     * All files found are filtered by an IOFileFilter.
     *
     * @param strdirpath         the strdirpath
     * @param WildcardFileFilter filter to apply when finding files. such as "*.*"
     * @return list list
     */
    public static List<String> listFileNamesInDirectoryRecursive(
            String strdirpath, String WildcardFileFilter) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                new WildcardFileFilter(WildcardFileFilter),
                TrueFileFilter.INSTANCE);
        List<String> strFileNames = new ArrayList<String>();
        for (File file : Files) {
            strFileNames.add(IOHelper.getBaseName(file.getName()));
        }
        return strFileNames;
    }

    /**
     * Finds files within a given directory (and its subdirectories) which match
     * an array of extensions.
     *
     * @param strdirpath the strdirpath
     * @param extensions an array of extensions.such as new String[]{"java","xml"}
     * @return list list
     */
    public static List<String> listFilesInDirectoryRecursive(String strdirpath,
                                                             String[] extensions) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                extensions, true);
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Finds files within a given directory (and its specified subdirectories).
     * All files found are filtered by an IOFileFilter.
     *
     * @param strdirpath         the strdirpath
     * @param WildcardFileFilter filter to apply when finding files. such as "*.*"
     * @param WildcardDirFilter  filter to apply when finding directory. such as "*"
     * @return list list
     */
    public static List<String> listFilesInDirectoryRecursive(String strdirpath,
                                                             String WildcardFileFilter, String WildcardDirFilter) {
        File directory = new File(strdirpath);
        List<File> Files = (List<File>) FileUtils.listFiles(directory,
                new WildcardFileFilter(WildcardFileFilter),
                new WildcardFileFilter(WildcardDirFilter));
        List<String> strFiles = new ArrayList<String>();
        for (File file : Files) {
            strFiles.add(file.getAbsolutePath());
        }
        return strFiles;
    }

    /**
     * Moves a file
     *
     * @param strSrcFilePath  the str src file path
     * @param strDestFilePath the str dest file path
     */
    public static void moveFile(String strSrcFilePath, String strDestFilePath) {
        File src = new File(strSrcFilePath);
        File dest = new File(strDestFilePath);
        try {
            FileUtils.moveFile(src, dest);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Moves a file to a directory.
     *
     * @param strSrcFilePath the str src file path
     * @param strDestDirPath the str dest dir path
     */
    public static void moveFileToDirectory(String strSrcFilePath,
                                           String strDestDirPath) {
        File src = new File(strSrcFilePath);
        File dest = new File(strDestDirPath);
        try {
            FileUtils.moveFileToDirectory(src, dest, true);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Moves a directory to the destination directory.
     *
     * @param strSrcDirPath  the str src dir path
     * @param strDestDirPath the str dest dir path
     */
    public static void moveDirectoryUnderDirectory(String strSrcDirPath,
                                                   String strDestDirPath) {
        File src = new File(strSrcDirPath);
        File dest = new File(strDestDirPath);
        try {
            FileUtils.moveDirectoryToDirectory(src, dest, true);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Reads the contents of a file line by line to a List of Strings
     *
     * @param strfilepath the strfilepath
     * @return list list
     */
    public static List<String> readLinesToList(String strfilepath) {

        File file = new File(strfilepath);
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return lines;
    }

    /**
     * Reads the contents of a file into a String
     *
     * @param strFilePath the str file path
     * @return string string
     */
    public static String readFileToString(String strFilePath) {
        File file = new File(strFilePath);
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return null;
    }

    /**
     * Writes a List of Strings to the specified File line by line.
     *
     * @param strfilepath the strfilepath
     * @param lines       the lines
     */
    public static void writeLinesToFile(String strfilepath, List<String> lines) {

        File file = new File(strfilepath);
        try {
            FileUtils.writeLines(file, "UTF-8", lines);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param str         the str
     * @param strFilePath the str file path
     */
    public static void writeStringToFile(String str, String strFilePath) {
        File file = new File(strFilePath);
        try {
            FileUtils.writeStringToFile(file, str, "UTF-8");
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
