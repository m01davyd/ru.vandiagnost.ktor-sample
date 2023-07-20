package component

import SensorsDTO
import StaticticsDTO
import kotlinx.js.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.useNavigate
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


external interface SensorsProps:Props{
    var sensors: List<SensorsDTO>
    var statictics: List<StaticticsDTO>
    var deletes: (Int) -> Unit
}

val Sensors = FC<SensorsProps>("Sensors") { props ->
    val (isDropdownOpen, setIsDropdownOpen) = useState(false)
    fun toggleDropdown() {
        setIsDropdownOpen(!isDropdownOpen)
    }
    val (isDropdownOpen1, setIsDropdownOpen1) = useState(false)
    fun toggleDropdown1() {
        setIsDropdownOpen1(!isDropdownOpen1)
    }
    val (isClickIn, setIsClickIn) = useState(false)
    val navigate = useNavigate()
    val authContext = useContext(AuthContext)
           div {
                id = "leftsensors"
               div {
                   id="profili"
                   h1{
                       id="username"
                       +"${authContext.username}"
                   }
                   if (isDropdownOpen1) {
                       img {
                           onClick = { toggleDropdown1() }
                           id = "clouse1"
                           src = "/static/clouse.png"
                       }
                       h2{
                           id="redactor"
                           +"Редактировать профиль"
                           onClick={
                               navigate("/redactor/${authContext.username}")
                           }
                       }
                       h2{
                           id="exit"
                           onClick={
                               navigate("/")
                               authContext.setUsername(null)
                           }
                           +"Выход"
                       }
                   } else {
                       img {
                           onClick = { toggleDropdown1() }
                           id = "open1"
                           src = "/static/open.png"
                       }
                   }
               }
                img {
                    id = "lines"
                    src = ("/static/lines.png")
                }
                div {
                    id = "sensorsList"
                    h1 {
                        id = "Smart"
                        +"Smart-датчики"
                        onClick={
                            navigate("/sensors")
                        }
                    }
                    ul {
                        id = "ulSmart"
                        if (isDropdownOpen) {
                            props.sensors.map { sensor ->
                                li {
                                    id = "liSmart"
                                    key = sensor.name
                                    onClick={
                                        navigate("/stat/${sensor.name}")
                                    }
                                    +"№ ${sensor.name}"
                                }
                            }
                        }
                    }
                    if (isDropdownOpen) {
                        img {
                            onClick = { toggleDropdown() }
                            id = "clouse"
                            src = "/static/clouse.png"
                        }
                    } else {
                        img {
                            onClick = { toggleDropdown() }
                            id = "open"
                            src = "/static/open.png"
                        }
                    }
                }
                img {
                    id = "fvagons"
                    src = ("/static/fvagons.png")
                }
            }
            div {
                id = "rightSensors1"
                button {
                    id = "plus"
                    type = ButtonType.submit
                    +"ДОБАВИТЬ МОДУЛЬ"
                    onClick = {
                        setIsClickIn(value = true)
                    }
                }
                if (isClickIn == true) {
                    navigate("/sensorsAdd")
                }
                props.statictics.mapIndexed { index, stItem ->
                    div {
                        id = "divLi"
                        div {
                            id = "divLi2"
                            img {
                                id = "delete"
                                src = "/static/delete.png"
                                onClick={
                                    props.deletes(stItem.sensorsId)
                                }
                            }
                            h2 {
                                id = "h2"
                                +"№ ${stItem.sensorsId}"
                            }
                            h3 {
                                id = "h3"
                                +"Состояние вагона: ${stItem.trackCondition}"
                                br {}
                                +"Долгота: ${stItem.longitude}"
                                br {}
                                +"Широта: ${stItem.latitude}"
                                br {}
                                +"Заряд: ${stItem.batteryCharge}"
                                br {}
                                +"Температура: ${stItem.temperature}"
                            }
                        }

                    }

                }
            }
    }

val sensorContainer = FC("SensorsContainer") { _: Props ->
    val queryClient = useQueryClient()

    val statisticsQueryKey = arrayOf("Statistics").unsafeCast<QueryKey>()

    val sensorsQueryKey = arrayOf("Sensors").unsafeCast<QueryKey>()
    val sensorsQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = sensorsQueryKey,
        queryFn = {
            fetchText("/sensors")
        }
    )
    val statisticsQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = statisticsQueryKey,
        queryFn = {
            fetchText("/stat")
        }
    )
    val sensorsDeleteQueryKey = arrayOf("Delete").unsafeCast<QueryKey>()
    val delete = useMutation<HTTPResult, Any, Int, Any>(
        mutationFn = { sensorsId   ->
            fetch(
                ("/delete/$sensorsId"),
                jso {
                    method = "DELETE"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(sensorsId )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(sensorsDeleteQueryKey)
            }
        }
    )

    if ( statisticsQuery.isLoading || sensorsQuery.isLoading) {
        div { +"Loading .." }
    } else if (statisticsQuery.isError || sensorsQuery.isError) {
        div { +"Error!" }
    } else {
        val sensorsData: List<SensorsDTO> = Json.decodeFromString(sensorsQuery.data ?: "")
        val statisticsData: List<StaticticsDTO> = Json.decodeFromString(statisticsQuery.data ?: "")

        Sensors {
            sensors = sensorsData
            statictics = statisticsData
            deletes={ sensorsId ->
                delete.mutate(sensorsId, null)
            }

        }
    }
}
