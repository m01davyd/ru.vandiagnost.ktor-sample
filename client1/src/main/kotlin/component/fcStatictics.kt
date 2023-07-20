package component

import SensorsDTO
import StaticticsDTO
import io.data2viz.geom.Point
import kotlinx.js.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.hr
import react.router.useNavigate
import react.router.useParams
import react.useContext
import react.useState
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json
import kotlin.random.Random

private val width = 200.0
private val height = 200.0


val randomPoints = (1 .. 1000).map{ Point(Random.nextDouble(), Random.nextDouble()) }

private val values = listOf(1.0, 2.0, 3.0, 4.0, 5.0)
private val values1 = listOf(3.0, 2.0, 5.0, 3.0, 6.0)
external interface StaticticsProps: Props {
    var sensors: List<SensorsDTO>
    var statictics: List<StaticticsDTO>
    var statIn: List<StaticticsDTO?>
    var deleteSensor: (String)->Unit
}

val Statictics = FC<StaticticsProps>("Statictics") { props ->
    val (isDropdownOpen, setIsDropdownOpen) = useState(false)
    val (isOpen, setIsOpen) = useState(false)
    val (isClickIn, setIsClickIn) = useState(false)

    fun toggleOpen() {
        setIsOpen(!isOpen)
    }
    fun toggleDropdown() {
        setIsDropdownOpen(!isDropdownOpen)
    }
    val (isDropdownOpen1, setIsDropdownOpen1) = useState(false)
    fun toggleDropdown1() {
        setIsDropdownOpen1(!isDropdownOpen1)
    }
    val authContext = useContext(AuthContext)
    val navigate = useNavigate()
    ReactHTML.div {

        id = "leftsensors"

        ReactHTML.div {
            id = "profili"
            ReactHTML.h1 {
                id = "username"
                +"${authContext.username}"
            }
            if (isDropdownOpen1) {
                ReactHTML.img {
                    onClick = { toggleDropdown1() }
                    id = "clouse1"
                    src = "/static/clouse.png"
                }
                ReactHTML.h2 {
                    id = "redactor"
                    +"Редактировать профиль"
                }
                ReactHTML.h2 {
                    id = "exit"
                    onClick={
                        navigate("/")
                        authContext.setUsername(null)
                    }
                    +"Выход"
                }
            } else {
                ReactHTML.img {
                    onClick = { toggleDropdown1() }
                    id = "open1"
                    src = "/static/open.png"
                }
            }


        }
        ReactHTML.img {
            id = "lines"
            src = ("/static/lines.png")
        }
        ReactHTML.div {
            id = "sensorsList"
            ReactHTML.h1 {
                id = "Smart"
                onClick={
                    navigate("/sensors")
                }
                +"Smart-датчики"
            }
            ReactHTML.ul {
                id = "ulSmart"
                if (isDropdownOpen) {
                    props.sensors.map { sensor ->
                        ReactHTML.li {
                            id = "liSmart"
                            key = sensor.name
                            +"№ ${sensor.name}"
                            onClick={
                                navigate("/stat/${sensor.name}")
                            }
                        }
                    }
                }
            }
            if (isDropdownOpen) {
                ReactHTML.img {
                    onClick = { toggleDropdown() }
                    id = "clouse"
                    src = "/static/clouse.png"
                }
            } else {
                ReactHTML.img {
                    onClick = { toggleDropdown() }
                    id = "open"
                    src = "/static/open.png"
                }
            }
        }
        ReactHTML.img {
            id = "fvagons"
            src = ("/static/fvagons.png")
        }
    }
    ReactHTML.div {

        val groupsParams = useParams()
        val name = groupsParams["name"] ?: "Route param error"
        id = "rightSensors"
        ReactHTML.button {
            id = "plus"
            type = ButtonType.submit
            +"ДОБАВИТЬ МОДУЛЬ"
            onClick = {
                setIsClickIn(value = true)
            }
        }
        ReactHTML.button {
            id = "deleteM"
            type = ButtonType.submit
            +"УДАЛИТЬ МОДУЛЬ"
            onClick = {
                props.deleteSensor(name)
                navigate("/sensors")
            }
        }
        if (isClickIn == true) {
            navigate("/sensorsAdd")
        }

        props.statictics.mapIndexed { index, stItem ->

            ReactHTML.div {
                id = "divStat"
                ReactHTML.h2 {
                    id = "h2Stat"
                    +"Smart-датчик № ${stItem.sensorsId}"
                }
                hr {
                    id = "stat"
                }


                ReactHTML.h3 {
                    id = "h3Stat"
                    +"Состояние вагона: ${stItem.trackCondition}"
                    ReactHTML.br {}
                    +"Долгота: ${stItem.longitude}"
                    ReactHTML.br {}
                    +"Широта: ${stItem.latitude}"
                    ReactHTML.br {}
                    +"Заряд: ${stItem.batteryCharge}"
                    ReactHTML.br {}
                    +"Температура: ${stItem.temperature}"
                }
                h3 {
                    id = "map"
                    +"Карта"
                }

            }
   /* val vc = newVizContainer().apply {
        size = Size(width, height)
    }

    // Chart DSL, our domain object is a "Point"

    vc.chart(randomPoints) {

        config {
            // Display the cursor
            cursor {
                show = true
            }
        }

        // Create 2 continuous numeric dimensions
        val xPosition = quantitative( { domain.x } )
        val yPosition = quantitative( { domain.y } )

        // Plot values

        plot(xPosition, yPosition)
    }

    */

}


                }

        }




val staticticsContainer = FC("StaticticsContainer") { _: Props ->
    val queryClient = useQueryClient()
    val groupsParams = useParams()
    val name = groupsParams["name"] ?: "Route param error"
    val sensorsQueryKey = arrayOf("Sensors").unsafeCast<QueryKey>()
    val statisticsQueryKey = arrayOf("Statistics").unsafeCast<QueryKey>()
    val usersDQueryKey = arrayOf("DeleteD").unsafeCast<QueryKey>()
    val statisticsInQueryKey = arrayOf("StatisticsIn").unsafeCast<QueryKey>()
    val sensorsQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = sensorsQueryKey,
        queryFn = {
            fetchText("/sensors")
        }
    )
    val deleteSensorQ = useMutation<HTTPResult, Any, String, Any>(
        mutationFn = { name   ->
            fetch(
                ("/delete/d/$name"),
                jso {
                    method = "DELETE"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(name)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(usersDQueryKey)
            }
        }
    )
    val statisticsQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = statisticsQueryKey,
        queryFn = {
            fetchText("/stat")
        }
    )

    val statisticsInQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = statisticsInQueryKey,
        queryFn = {
            fetchText("/stat/${name}")
        }
    )

    if (deleteSensorQ.isLoading ||statisticsQuery.isLoading || sensorsQuery.isLoading|| statisticsInQuery.isLoading) {
        ReactHTML.div { +"Loading .." }
    } else if (deleteSensorQ.isError || statisticsQuery.isError || sensorsQuery.isError|| statisticsInQuery.isError) {
        ReactHTML.div { +"Error!" }
    } else {
        val sensorsData: List<SensorsDTO> = Json.decodeFromString(sensorsQuery.data ?: "")
        val statisticsData: List<StaticticsDTO> = Json.decodeFromString(statisticsQuery.data ?: "")
        val statisticsDataIn: List<StaticticsDTO?> = Json.decodeFromString(statisticsInQuery.data ?: "")

        Statictics {
            sensors = sensorsData
            statictics = statisticsData
            statIn= statisticsDataIn
            deleteSensor={ sensorsname ->
                deleteSensorQ.mutate(sensorsname, null)
            }
        }
    }
}


