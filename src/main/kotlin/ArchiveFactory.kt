class ArchiveFactory : ElementFactory<Archive> {
    override fun create(vararg args: String): Archive = Archive(args[0])
}

