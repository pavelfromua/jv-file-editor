public enum ConsoleCommands {
    CREATE("create [path] [file-name] Создает текстовый файл по указанному пути. "
            + "Если путь не существует, вывести соответствующее сообщение. "
            + "Если файл уже существует, вывести запрос на его перезапись"),
    READ("read [path] [file-name] Считывает файл по указанному пути и выводит текст в консоль. "
            + "Если указанного пути и/или файла не существует, вывести соответствующее сообщение"),
    INFO("info [path] [file-name] Выводит краткую информацию по указанному файлу: количество "
            + "символов, строк, слов, дату и время последнего изменения, размер файла"),
    HELP("help Выводит в консоль все доступные комманды и информацию к ним\n"
            + "\n help [command] Выводит в консоль информацию по указанной команде"),
    EXIT("exit Завершение работы программы");

    private final String description;

    ConsoleCommands(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
