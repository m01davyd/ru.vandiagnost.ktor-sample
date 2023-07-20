import io.data2viz.charts.chart.chart
import io.data2viz.charts.chart.mark.dot
import io.data2viz.charts.chart.quantitative
import io.data2viz.charts.viz.newVizContainer
import io.data2viz.geom.Size
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.id
import org.w3c.dom.Node

private val width = 300.0
private val height = 300.0

private val values = listOf(1.0, 2.0, 3.0, 4.0, 5.0)

fun Node.myFirstChart() {
    append {
        div {
            id="graf"
            newVizContainer().apply {
                size = Size(width, height)
                chart(values) {
                    val values = quantitative({ domain })
                    dot(values, values)
                }
            }
        }
    }
}