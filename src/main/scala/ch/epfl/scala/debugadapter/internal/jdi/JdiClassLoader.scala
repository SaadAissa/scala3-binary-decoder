package ch.epfl.scala.debugadapter.internal.jdi

import ch.epfl.scala.debugadapter.internal.binary
import ch.epfl.scala.debugadapter.internal.binary.ClassType

import java.util as ju
import scala.jdk.CollectionConverters.*

class JdiClassLoader(obj: Any)
    extends binary.BinaryClassLoader
    with JavaReflection(obj, "com.sun.jdi.ClassLoaderReference"):
  override def loadClass(name: String): ClassType =
    visibleClasses.find(_.name == name).get

  private def visibleClasses: Seq[JdiReferenceType] =
    invokeMethod[ju.List[Any]]("visibleClasses").asScala.map(JdiReferenceType.apply(_)).toSeq
