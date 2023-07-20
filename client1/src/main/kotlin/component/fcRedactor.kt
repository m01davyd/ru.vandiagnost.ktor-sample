package component
import SensorsDTO
import StaticticsDTO
import UserDTO
import kotlinx.js.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.useNavigate
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

external interface RedactorProps:Props{
    var sensors: List<SensorsDTO>
    var statictics: List<StaticticsDTO>
    var deleteSensor: (String)->Unit
    var update: (Int) -> Unit
    var updateUser: (UserDTO) -> Unit
    var user: UserDTO
}

val RedactorP = FC<RedactorProps>("Redactor") { props ->

    val (isDropdownOpen, setIsDropdownOpen) = useState(false)
    fun toggleDropdown() {
        setIsDropdownOpen(!isDropdownOpen)
    }

    val (newname, setNewName) = useState("")
    val (newpass, setNewPass) = useState("")
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
            id = "profili"
            h1 {
                id = "username"
                +"${authContext.username}"
            }
            if (isDropdownOpen1) {
                img {
                    onClick = { toggleDropdown1() }
                    id = "clouse1"
                    src = "/static/clouse.png"
                }
                h2 {
                    id = "redactor"
                    +"Редактировать профиль"
                }
                h2 {
                    id = "exit"
                    onClick = {
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
                onClick = {
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
                            onClick = {
                                navigate("/stat/${sensor.name}")
                            }
                            +sensor.name

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
        id = "rightSensors"
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
        ReactHTML.div {
            id = "divStat"
            ReactHTML.h2 {
                id = "h2Stat"
                +"Профиль "
            }
            ReactHTML.hr {
                id = "stat"
            }
            label {
                htmlFor = "nname"
                +"Имя пользователя"
            }
            input {
                id = "nname"
                type = InputType.text
                value = name
                onChange = { event ->
                    setNewName(event.target.value)
                }

            }
            label {
                htmlFor = "npass"
                +"Пароль"
            }
            input {
                id = "npass"
                type = InputType.text
                value = name
                onChange = { event ->
                    setNewPass(event.target.value)
                }

            }
            val authContext = useContext(AuthContext)
            button {
                type = ButtonType.button
                +"Обновить профиль"
                onClick = {
                    val updatedUser = UserDTO(
                        name = newname,
                        password = newpass,
                        email = props.user.email,
                        createdAt = props.user.createdAt,
                        updateAt = props.user.updateAt,
                        role = props.user.role
                    )
                    authContext.setUsername(newname)
                    props.updateUser(updatedUser) // Вызов функции обновления пользователя из пропсов
                }

            }
        }
    }
}

    val redactorContainer = FC("RedactorContainer") { _: Props ->
        val queryClient = useQueryClient()
        val authContext = useContext(AuthContext)
        val sensorsQueryKey = arrayOf("Sensors").unsafeCast<QueryKey>()
        val usersQueryKey = arrayOf("Users").unsafeCast<QueryKey>()

        val sensorsQuery = useQuery<String, QueryError, String, QueryKey>(
            queryKey = sensorsQueryKey,
            queryFn = {
                fetchText("/sensors")
            }
        )

        val update = useMutation<HTTPResult, Any, UserDTO, Any>(
            mutationFn = { updatedSensor: UserDTO ->
                fetch(
                    "/redactor/${authContext.username}",
                    jso {
                        method = "PUT"
                        headers = json("Content-Type" to "application/json")
                        body = Json.encodeToString(updatedSensor)
                    }
                )
            },
            options = jso {
                onSuccess = { _: Any, _: Any, _: Any? ->
                    queryClient.invalidateQueries<Any>(usersQueryKey)
                }
            }
        )

        val updateSensor = useCallback  { updatedSensor: UserDTO ->
            update.mutate(updatedSensor, jso { /* options configuration */ })
        }


        if (sensorsQuery.isLoading) {
            div { +"Loading .." }
        } else if (sensorsQuery.isError) {
            div { +"Error!" }
        } else {
            val sensorsData: List<SensorsDTO> = Json.decodeFromString(sensorsQuery.data ?: "")
            RedactorP{
                sensors = sensorsData

            }
        }
    }

