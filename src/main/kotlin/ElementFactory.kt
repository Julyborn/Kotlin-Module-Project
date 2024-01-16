fun interface ElementFactory<T : Titled> {
    fun create(vararg args: String): T
}