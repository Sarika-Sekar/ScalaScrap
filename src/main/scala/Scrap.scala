import com.themillhousegroup.scoup.{Scoup,ScoupImplicits}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source

object Texas extends App with ScoupImplicits{
  val url = Scoup.get("https://comptroller.texas.gov/taxes/sales/county.php")
  val waitUrl = Await.result(url,Duration.Inf)
  val docHtml = Scoup.parseHTML(waitUrl)
  val tableCon = docHtml.select("table")
  val tableHeader = tableCon.select("thead tr th").map(_.text.trim).toList
  val tableContent = tableCon.select("tbody tr").toList.map{row =>
    val column = row.select("td")
    Map(
      tableHeader.head -> row.select("th").text(),
      tableHeader(1) -> column.get(0).text(),
      tableHeader(2) -> column.get(1).text()
    ).toList
  }
  println(tableContent)
}
