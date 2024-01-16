import java.util.Scanner


// Возможно я слишком буквально воспринял пункт: "Не должно быть повторений одного и того же кода"
// функции спроектированы так, что бы минимизировать дублирование
class MenuManager {
    private val scanner = Scanner(System.`in`)

    fun start() {
        val archives = mutableListOf<Archive>()
        menu("Список архивов", archives, ArchiveFactory()) { archive ->
            menu("Список заметок", archive.notes, NoteFactory()) { note ->
                noteView(note)
            }
        }
    }

    private fun <T : Titled> menu(title: String, items: MutableList<T>, factory: ElementFactory<T>, additionalAction: (T) -> Unit) {
        while (true) {
            showOptions(title, items)
            if (!chooseOption(items, factory, additionalAction)) break
        }
    }

    private fun <T : Titled> chooseOption(items: MutableList<T>, factory: ElementFactory<T>, additionalAction: (T) -> Unit): Boolean {
        when (val choice = getUserInput(items.size + 1)) {
            0 -> addElement(items, factory)
            in 1..items.size -> additionalAction(items[choice - 1])
            items.size + 1 -> return false
        }
        return true
    }

    private fun noteView(note: Note) {
        println("Имя заметки: ${note.title}\nТекст заметки: ${note.text}")
    }

    private fun <T : Titled> showOptions(title: String, items: List<T>) {
        println("$title:")
        println("0. Создать")
        items.forEachIndexed { index, item ->
            println("${index + 1}. ${item.title}")
        }
        println("${items.size + 1}. Выход")
    }

    //ниже идёт проблемная функция из-за стирания типов и активного использования дженериков в других методах (inline не сработает)
    //в итоге я перебрал разные варианты решения этой проблемы, и остановился на использовании фабричного метода
    private fun <T : Titled> addElement(items: MutableList<T>, factory: ElementFactory<T>) {
        println("Введите название:")
        val title = getUserInput()
        val newElement = if (factory is NoteFactory) {
            println("Введите текст заметки:")
            val text = getUserInput()
            factory.create(title, text)
        } else {
            factory.create(title)
        }

        items.add(newElement as T)
        println("Элемент '${newElement.title}' добавлен.")
    }

    private fun getUserInput(maxDigit: Int): Int {
        val number = scanner.nextLine().toIntOrNull()
        when (number) {
            null -> println("Текст не является числом")
            in 0..maxDigit -> return number
            else -> println("Число должно быть в диапазоне от 0 до $maxDigit")
        }
        return -1
    }

    private fun getUserInput():String{
        while (true) {
            val text = scanner.nextLine()
            if (text.isNotBlank()) {
                return text
            }
            println("Необходимо ввести какой-то текст")
        }
    }
}


