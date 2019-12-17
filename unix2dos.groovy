/** Convert line ending in all files from Unix-style to DOS style. */
args.each {
  fileName ->
  def f = new File(fileName);
  assert f.exists();
  f.text = f.text.replaceAll('\r?\n', '\r\n');
}
