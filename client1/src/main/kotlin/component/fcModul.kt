package component



import SensorsDTO
import SensorsNew
import kotlinx.js.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
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

external interface ModulProps:Props{
    var sensors: List<SensorsDTO>
    var saveElement: (SensorsNew) -> Unit
}

val Modul = FC<ModulProps>("Modul") { props ->
    val (isDropdownOpen, setIsDropdownOpen) = useState(false)
    val (isOpen, setIsOpen) = useState(false)
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
    val navigate= useNavigate()
    val authContext = useContext(AuthContext)
    div{
        id="leftsensors"
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
                img {
                    onClick = { toggleDropdown1() }
                    id = "open1"
                    src = "/static/open.png"
                }
            }
        }
        img{
            id="lines"
            src=("/static/lines.png")
        }
        div{
            id="sensorsList"
            h1{
                id="Smart"
                onClick={
                    navigate("/sensors")
                }
                +"Smart-датчики"
            }
            ul {
                id="ulSmart"
                if (isDropdownOpen) {
                    props.sensors.map { sensor ->
                        li {
                            id="liSmart"
                            key = sensor.name
                            +"№ ${sensor.name}"
                        }
                    }
                }
            }
            if (isDropdownOpen) {
                img{
                    onClick={ toggleDropdown() }
                    id="clouse"
                    src="/static/clouse.png"
                }
            } else {
                img{
                    onClick={ toggleDropdown() }
                    id="open"
                    src="/static/open.png"
                }
            }
        }
        img{
            id="fvagons"
            src=("/static/fvagons.png")
        }
    }
    div{
        id="rightSensors"
        div{
            id="rightSensors2"

        ReactHTML.button {
            id = "sublimit3"
            +"СТАТИСТИКА"
            onClick = {
                navigate(-1)
            }
        }
        val (modul, setModule) = useState("")
        val (code, setCode) = useState("")
        ReactHTML.div {
            id = "divForm2"
            ReactHTML.form {
                id="formModul"
                action = "/sensorsAdd"
                ReactHTML.div {
                    id = "divLogin2"
                    ReactHTML.h1 {
                        id="Login2"//////?????
                        +"Добавить модуль"
                    }
                    ReactHTML.hr {}
                    ReactHTML.label {
                        htmlFor = "login2"
                        +"Имя модуля"
                    }
                    ReactHTML.input {
                        id = "login2"
                        type = InputType.text
                        value = modul
                        onChange = { event ->
                            setModule(event.target.value)
                        }
                    }
                    ReactHTML.label {
                        htmlFor = "pass2"
                        +"Код доступа"
                    }
                    ReactHTML.input {
                        id = "pass2"
                        if(isOpen){
                            type = InputType.password
                        }
                        else{
                            type = InputType.text
                        }
                        value = code
                        onChange = { event ->
                            setCode(event.target.value)
                        }
                        onClick={ toggleOpen()}
                    }
                    ReactHTML.button {
                        id = "sublimit2"
                        type = ButtonType.submit
                        +"Добавить модуль"
                        onClick = {
                            props.saveElement(SensorsNew(modul, code))
                            navigate("/sensors")
                             }
                         }
                     }
                }
            }
        }
    }
}

val ModulContainer = FC("ModulContainer") { _: Props ->
    val queryClient = useQueryClient()

    val sensorsQueryKey = arrayOf("Sensors").unsafeCast<QueryKey>()
    val modulQueryKey = arrayOf("Modul").unsafeCast<QueryKey>()

    val sensorsQuery = useQuery<String, QueryError, String, QueryKey>(
        queryKey = sensorsQueryKey,
        queryFn = {
            fetchText("/sensors")
        }
    )
    val addModul = useMutation<HTTPResult, Any, SensorsNew, Any>(
        mutationFn = { element: SensorsNew ->
            fetch(
                ("/sensorsAdd"),
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(element)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(modulQueryKey)
            }
        }
    )

    if (addModul.isLoading || sensorsQuery.isLoading) {
        div { +"Loading .." }
    } else if (addModul.isError || sensorsQuery.isError) {
        div { +"Error!" }
    } else {
        val sensorsData: List<SensorsDTO> = Json.decodeFromString(sensorsQuery.data ?: "")

        Modul {
            sensors = sensorsData
            saveElement = { modulData ->
                addModul.mutate(modulData, null) // Добавляем параметр options
            }
        }
    }
}
