package ch.epfl.scala.debugadapter.internal.jdi

import ch.epfl.scala.debugadapter.internal.binary.*

import java.lang.reflect.InvocationTargetException
import scala.jdk.CollectionConverters.*
import java.util as ju

class JdiMethod(val obj: Any) extends JavaReflection(obj, "com.sun.jdi.Method") with Method:
  override def name: String = invokeMethod("name")

  override def declaringClass: JdiReferenceType =
    JdiReferenceType(invokeMethod("declaringType"))

  override def allParameters: Seq[Parameter] =
    invokeMethod[java.util.List[Object]]("arguments").asScala.toSeq.map(JdiLocalVariable.apply(_))

  override def returnType: Option[Type] =
    try Some(JdiType(invokeMethod("returnType")))
    catch
      case e: InvocationTargetException if e.getCause.getClass.getName == "com.sun.jdi.ClassNotLoadedException" =>
        None

  override def returnTypeName: String = invokeMethod("returnTypeName")

  override def isBridge: Boolean = invokeMethod("isBridge")

  override def isStatic: Boolean = invokeMethod("isStatic")

  override def isFinal: Boolean = invokeMethod("isFinal")

  override def isConstructor: Boolean = invokeMethod("isConstructor")

  override def sourceLines: Option[SourceLines] =
    Some(SourceLines(declaringClass.sourceName, allLineLocations.map(_.lineNumber)))

  override def signedName: SignedName = SignedName(name, signature)

  override def instructions: Seq[Instruction] =
    ByteCodes.parse(bytecodes, declaringClass.constantPool)

  private def allLineLocations: Seq[JdiLocation] =
    invokeMethod[ju.List[Any]]("allLineLocations").asScala.map(JdiLocation.apply(_)).toSeq

  private def signature: String = invokeMethod("signature")

  private def bytecodes: Array[Byte] = invokeMethod("bytecodes")
