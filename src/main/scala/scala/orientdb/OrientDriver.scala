package scala.orientdb

import connection.monadic.{WithConnectionConversion, WithConnectionTConversions, DocumentHelper}
import types.OrientDBTypes

object OrientDriver extends DocumentHelper
  with OrientDBTypes
  with WithConnectionTConversions
  with WithConnectionConversion

