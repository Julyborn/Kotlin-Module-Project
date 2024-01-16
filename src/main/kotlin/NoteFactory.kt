class NoteFactory : ElementFactory<Note> {
    override fun create(vararg args: String): Note = Note(args[0], if (args.size > 1) args[1] else "")
}