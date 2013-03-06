package scala.orientdb.graph

import com.orientechnologies.orient.core.command.traverse.OTraverse
import com.orientechnologies.orient.core.command.{OCommandPredicate, OCommandContext}
import collection.JavaConversions
import JavaConversions._

import orientdb.types.OId
import orientdb.document.ODocumentS
import com.orientechnologies.orient.core.record.ORecord
import com.orientechnologies.orient.core.record.impl.ODocument
import orientdb.OrientDriver
import OrientDriver._

object Traverse {

  def apply(ids:Iterable[OId]) : Traverse = Traverse(()=>new OTraverse().target(ids.map(_.toRecordId).toArray : _*))

  def forIds(ids:OId*) = apply(ids.toList)

  def forClass(name:String) = browse(name).map(_.id).value.map(apply)

}

case class Traverse(create: ()=>OTraverse) {

  def predicate(condition:ODocumentS=>Boolean) =
    map(_.predicate(new OCommandPredicate {
      def evaluate(iRecord: ORecord[_], iCurrentResult: ODocument, iContext: OCommandContext) =
      //TODO??
        condition.apply(ODocumentS(iRecord.asInstanceOf[ODocument])) : java.lang.Boolean
    }))

  def predicate(condition:(ODocumentS,TraverseContext)=>Boolean) =
    map(_.predicate(new OCommandPredicate {
          def evaluate(iRecord: ORecord[_], iCurrentResult: ODocument, iContext: OCommandContext) =
            condition.apply(ODocumentS(iRecord.asInstanceOf[ODocument]), TraverseContext(iContext)) : java.lang.Boolean
    }))


  def field(names:String*) = map(_.fields(names))

  def iterate : WithConnectionI[ODocumentS] =
    pointT[Iterable,ODocumentS](create().iterator().toIterable.map(_.asInstanceOf[ODocument]).map(ODocumentS.apply))

  def limit(limit:Long) = map(_.limit(limit))

  def depth(limit:Long) = predicate((a:ODocumentS,b:TraverseContext)=>b.depth.getOrElse(0)<=limit)

  private def map(action: OTraverse=>OTraverse) = copy(create = () => action(create()))
}


case class TraverseContext(context:OCommandContext) {
  def depth = get("depth").map(_.toString.toInt)
  def path = get("path").map(_.toString)
  def get(name:String) = Option(context.getVariable(name))
}


