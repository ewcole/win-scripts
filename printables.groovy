args.each {
    arg ->
    def f = new File(arg);
    assert f.exists();
    f.collect {(it >= ' ' && it <= '~')?it:'.'}.each {print it}
}
