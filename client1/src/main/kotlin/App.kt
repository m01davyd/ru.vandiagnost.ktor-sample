
//import emotion.css.css

import component.*
import kotlinx.browser.document
import react.*
import react.dom.client.createRoot
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools


val invalidateRepoKey = createContext<QueryKey>()

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(container).render(App.create {})

}

val App = FC<Props>("App") {
    BrowserRouter {
        QueryClientProvider {
            client = QueryClient()
            val (username, setUsername) = useState<String?>(null)
            AuthContext.Provider(value = AuthContextData(username, { value -> setUsername(value) })) {
                Routes {
                    Route {
                        path = "/"
                        element = createElement(loginContainer)
                    }

                    Route {
                        path = "/sensors"
                        element = createElement(sensorContainer)
                    }
                    Route {
                        path = "/sensorsAdd"
                        element = createElement(ModulContainer)
                    }
                    Route {
                        path = "/stat/:name"
                        element = createElement(staticticsContainer)
                    }
                    Route{
                        path="/redactor/:username"
                        element=createElement(redactorContainer)
                    }
                }
            }
            ReactQueryDevtools { }
             }
        }
    }



