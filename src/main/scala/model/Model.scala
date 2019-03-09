import java.io.PrintWriter

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.slf4j.LoggerFactory

import com.github.fommil.netlib.BLAS

import breeze.linalg.DenseVector
import vegas._
import vegas.DSL.SpecBuilder
import vegas.spec.Spec.FieldDef
import scala.annotation.meta.field

object Breeze1 extends App {

  def openWindow(link: String):Try[String] = {
   import scala.sys.process._
    Try{
      java.awt.Desktop.getDesktop.browse(new java.net.URI(link))
      link
    }
    .orElse(Try{
      s"open $link"!!;
      link
    })
    .orElse(Try{
      s"gio open $link"!!;
      link
    })
  }
  
  def display(plot: SpecBuilder) = {
    
    new PrintWriter("/tmp/out.html") { write(plot.html.pageHTML()); close };
    
    openWindow("/tmp/out.html") match {
      case Success(url) => println(s"sent to browser: $url")
      case Failure(e) => e.printStackTrace()
    }
  }
  
  
  override def main(args: Array[String]): Unit = {

    
    
    println("Init logging...");
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    val log = LoggerFactory.getLogger("main");
    println("Starting...");
    val b = BLAS.getInstance();
    println(s"BLAS = $b");
    val v = DenseVector(1, 2, 3, 4);
    println("Ending.");
    print(v)

    val plot = Vegas("Country Pop")
    .withData(
      Seq(
        Map("country" -> "USA", "population" -> 314),
        Map("country" -> "UK", "population" -> 64),
        Map("country" -> "DK", "population" -> 80)))
     .encodeX("country", Nom)
     .encodeY("population", Quant)
     .mark(Bar)

    display(plot)

    //pie(Set(0, 1, 9, 16, 4).toSeq.sorted)

  }
}