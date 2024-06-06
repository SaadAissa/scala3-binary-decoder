package ch.epfl.scala.decoder.javareflect

import ch.epfl.scala.decoder.binary

import java.lang.reflect.Parameter

class JavaReflectParameter(parameter: Parameter, loader: JavaReflectLoader) extends binary.Parameter:

  override def name: String = parameter.getName
  override def sourceLines: Option[binary.SourceLines] = None

  override def `type`: binary.Type = loader.loadClass(parameter.getType)

  override def toString: String = parameter.toString
