import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

xsdUrl = 'http://abbot.sourceforge.net/doc/abbot.xsd'
xmlUrl = 'http://abbot.sourceforge.net/src/example/SimpleApplet.xml'

new URL( xsdUrl ).withInputStream { xsd ->
  new URL( xmlUrl ).withInputStream { xml ->
    SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI )
                 .newSchema( new StreamSource( xsd ) )
                 .newValidator()
                 .validate( new StreamSource( xml ) )

  }
}

// XMLConstants.W3C_XML_SCHEMA_NS_URI ("http://www.w3.org/2001/XMLSchema")    W3C XML Schema 1.0
// XMLConstants.RELAXNG_NS_URI ("http://relaxng.org/ns/structure/1.0")        RELAX NG 1.0
