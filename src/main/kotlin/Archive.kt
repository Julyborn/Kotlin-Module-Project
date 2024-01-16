class Archive(override val title: String): Titled{
    val notes = mutableListOf<Note>()
}