import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class FileEditor {
    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);

        String fileData = "";
        while (true) {
            try {
                String consoleLine = reader.readLine();
                int commandSeparator = consoleLine.indexOf(" ");

                String commandName;
                if (commandSeparator != -1) {
                    commandName = consoleLine.substring(0, commandSeparator);
                } else {
                    commandName = consoleLine;
                }

                ConsoleCommands command;
                
                try {
                    command = ConsoleCommands.valueOf(commandName.toUpperCase());

                    if (commandSeparator != -1) {
                        consoleLine = consoleLine.substring(commandSeparator);
                    }
                } catch (Exception e) {
                    while (true) {
                        int answer = getAnswerYesNo("Хотите сохранить данные в файл?");
                        
                        if (answer != 0) {
                            if (answer == 1) {
                                command = ConsoleCommands.CREATE;
                                fileData = consoleLine;
                                
                                System.out.println("Введите путь к файлу и его имя для записи: ");
                                consoleLine = reader.readLine();

                            } else {
                                command = ConsoleCommands.EXIT;
                            }
                            break;
                        }
                    }
                }

                switch (command) {
                    case CREATE:
                        createFile(consoleLine, fileData);
                        break;
                    case READ:
                        readFile(consoleLine);
                        break;
                    case INFO:
                        infoFile(consoleLine);
                        break;
                    case HELP:
                        getHelp(consoleLine, commandSeparator);
                        break;
                    case EXIT:
                        throw new IOException();
                    default:
                        System.out.println("This is the file editor");
                        throw new IOException();
                }
            } catch (IOException e) {
                break;
            }
        }

        try {
            isr.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void infoFile(String consoleLine) {
        consoleLine = consoleLine.trim();
        int commandSeparator = consoleLine.indexOf(" ");

        if (commandSeparator == -1) {
            System.out.println("Не верный формат команды");
        } else {
            Path fullPath = Paths.get(consoleLine.substring(0, commandSeparator) + "\\"
                    + consoleLine.substring(commandSeparator).trim());
            Path path = fullPath.getParent();

            if (Files.exists(path)) {
                if (Files.exists(fullPath)) {
                    int symbolCount = 0;
                    int lineCount = 0;
                    int wordCount = 0;
                    Object dtOfLastChange = null;
                    long sizeInBytes = 0;

                    List<String> lines = null;
                    try {
                        lines = Files.readAllLines(fullPath);
                        lineCount = lines.size();
                        dtOfLastChange = Files.getLastModifiedTime(fullPath);
                        sizeInBytes = Files.size(fullPath);

                        if (lines.size() == 0) {
                            System.out.println("Файл пуст");
                        } else {
                            for (String string: lines) {
                                symbolCount += string.length();
                                wordCount += string.split("\\s").length;
                            }
                        }

                        System.out.println("Количество символов: " + symbolCount);
                        System.out.println("Количество строк:    " + lineCount);
                        System.out.println("Количество слов:     " + wordCount);
                        System.out.println("Дата/время последнего изменения: " + dtOfLastChange);
                        System.out.println("Размер в байтах:     " + sizeInBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Указанный файл не существует");
                }
            } else {
                System.out.println("Указанный путь не существует");
            }
        }
    }

    private static void readFile(String consoleLine) {
        consoleLine = consoleLine.trim();
        int commandSeparator = consoleLine.indexOf(" ");

        if (commandSeparator == -1) {
            System.out.println("Не верный формат команды");
        } else {
            Path fullPath = Paths.get(consoleLine.substring(0, commandSeparator) + "\\"
                    + consoleLine.substring(commandSeparator).trim());
            Path path = fullPath.getParent();

            if (Files.exists(path)) {
                if (Files.exists(fullPath)) {
                    List<String> lines = null;
                    try {
                        lines = Files.readAllLines(fullPath);
                        if (lines.size() == 0) {
                            System.out.println("Файл пуст");
                        } else {
                            for (String string: lines) {
                                System.out.println(string);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Указанный файл не существует");
                }
            } else {
                System.out.println("Указанный путь не существует");
            }
        }
    }

    private static void createFile(String consoleLine, String fileData) {
        consoleLine = consoleLine.trim();
        int commandSeparator = consoleLine.indexOf(" ");

        if (commandSeparator == -1) {
            System.out.println("Не верный формат команды");
        } else {
            Path fullPath = Paths.get(consoleLine.substring(0, commandSeparator) + "\\"
                    + consoleLine.substring(commandSeparator).trim());
            Path path = fullPath.getParent();
            Path fileName = fullPath.getFileName();

            if (Files.notExists(path)) {
                int answer = getAnswerYesNo("Путь \"" + path + "\" не существует. Создать?");

                if (answer == 1) {
                    try {
                        Files.createDirectory(path);
                    } catch (IOException e) {
                        System.out.println("Проверьте формат пути. "
                                + "Конечный каталог/файл не был создан");
                    }
                }
            }

            if (Files.exists(path)) {
                if (Files.exists(fullPath)) {
                    int answer = getAnswerYesNo("Файл \"" + fileName + "\" существует. "
                            + "Разрешить перезапись?");

                    if (answer == 1) {
                        try {
                            List<String> lines = Arrays.asList(fileData);
                            Files.write(fullPath, lines, StandardCharsets.UTF_8,
                                    StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Files.createFile(fullPath);
                        if (!fileData.isEmpty()) {
                            List<String> lines = Arrays.asList(fileData);
                            Files.write(fullPath, lines, StandardCharsets.UTF_8,
                                    StandardOpenOption.APPEND);
                        }
                    } catch (IOException e) {
                        System.out.println("Проверьте формат команды. "
                                + "Конечный каталог/файл не был создан");
                    }
                }
            }
        }
    }

    public static int getAnswerYesNo(String question) {
        System.out.println(question + " (y/n)");

        InputStreamReader reader = new InputStreamReader(System.in);
        char answer = 0;
        try {
            answer = (char) reader.read();
            if (answer == 'y' || answer == 'Y') {
                return 1;
            } else if (answer == 'n' || answer == 'N') {
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void getHelp(String consoleLine, int commandSeparator) {
        if (commandSeparator == -1) {
            printHelp();
        } else {
            printHelp(consoleLine.substring(commandSeparator));
        }
    }
    
    public static void printHelp() {
        for (ConsoleCommands command: ConsoleCommands.values()) {
            System.out.println(command.getDescription() + "\n");
        }
    }

    private static void printHelp(String subCommand) {
        try {
            ConsoleCommands command = ConsoleCommands.valueOf(subCommand.trim().toUpperCase());
            System.out.println(command.getDescription() + "\n");
        } catch (Exception e) {
            System.out.println(String.format("Command: %s is invalid", subCommand));
        }
    }
}
