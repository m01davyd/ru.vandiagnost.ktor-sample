package component


import kotlinx.js.jso
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import react.*
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.router.useNavigate
import ru.vandiagnost.login.LoginRecieveRemote
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import kotlin.js.json

data class AuthContextData(val username: String?, val setUsername: (String?) -> Unit)
val AuthContext = createContext(AuthContextData(null, {}))
external interface LoginProps: Props {
    var saveElement: (LoginRecieveRemote) -> Unit
}
val Login = FC<LoginProps>("Login") { props ->
    val navigate= useNavigate()
    val authContext = useContext(AuthContext)
    val (name, setName) = useState("")
    val (password, setPassword) = useState("")
    val (isOpen, setIsOpen) = useState(false)
    fun toggleOpen() {
        setIsOpen(!isOpen)
    }
    div {
        id = "fcLogin"
        ReactHTML.div {
            id = "divVagon"
            ReactHTML.img {
                id = "vagon"
                src = "static/vagons.png"
            }
        }
        ReactHTML.div {
            id = "divForm"
            ReactHTML.form {
                id = "formLogin"
                ReactHTML.div {
                    id = "divLogin"
                    ReactHTML.h1 {
                        id = "Login"
                        +"Войти"
                    }
                    ReactHTML.hr {}
                    ReactHTML.label {
                        htmlFor = "login"
                        +"Логин"
                    }
                    ReactHTML.input {
                        id = "login"
                        type = InputType.text
                        value = name
                        onChange = { event ->
                            setName(event.target.value)
                        }
                    }
                    ReactHTML.label {
                        htmlFor = "pass"
                        +"Пароль "
                    }
                    ReactHTML.input {
                        id = "pass"
                        if(isOpen){
                            type = InputType.password
                        }
                        else{
                            type = InputType.text
                        }
                        value = password
                        onChange = { event ->
                            setPassword(event.target.value)
                        }
                        onClick={ toggleOpen()}
                    }
                    ReactHTML.button {
                        id = "sublimit"
                        type = ButtonType.submit
                        +"Войти"
                        onClick = { event ->
                            event.preventDefault()
                            props.saveElement(LoginRecieveRemote(name, password))
                            authContext.setUsername(name)
                         }
                        }
                    }
                }
            }
        }
    }

val loginContainer = FC("LoginContainer") { _: Props ->
    val navigate = useNavigate()
    val queryClient = useQueryClient()
    val usersQueryKey = arrayOf("Login").unsafeCast<QueryKey>()
    val addMutation = useMutation<HTTPResult, Any, LoginRecieveRemote, Any?>(
        mutationFn = { element: LoginRecieveRemote ->
            fetch(
                ("/"),
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(element)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, data: Any? ->
                queryClient.invalidateQueries<Any>(usersQueryKey)
            }
        }
    )
    useEffect(dependencies = arrayOf(addMutation.isSuccess)) {
        if (addMutation.isSuccess) {
            val response = addMutation.data?.unsafeCast<Response>()
            val s= response!!.status.toInt()
            if (s == 200) {
                navigate("/sensors")

            } else {

                navigate("/")


            }
        }
    }
    if (addMutation.isLoading ) ReactHTML.div { +"Loading .." }
        else if (addMutation.isError ) ReactHTML.div { +"Error!" }
        else {
                Login {
                    saveElement = { ldata ->
                        addMutation.mutate(ldata, null)
                    }
                }
        }
    }



