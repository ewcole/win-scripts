args.each {
    arg ->
    def f = new File(arg);
    assert f.exists();
    f.text.collect {(it >= ' ' && it <= '~')?it:'.'}.each {print it}
}
