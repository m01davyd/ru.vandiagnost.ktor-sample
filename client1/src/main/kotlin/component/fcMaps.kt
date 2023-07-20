//package component
//
//
//import react.dom.h1
//import react.dom.render
//
//import kotlinext.js.jsObject
//import leaflet.L.Icon
//import leaflet.L.LatLng
//import leaflet.L.Map
//import leaflet.L.TileLayer
//import leaflet.L.marker
//import
//import react.*
//import react.dom.html.ReactHTML.div
//import reactleaflet.LatLng
//import reactleaflet.MapContainer
//import reactleaflet.TileLayer
//import reactleaflet.Marker
//import reactleaflet.useMap
//import reactleaflet.useMarkers
//import kotlin.js.Date
//
//interface MapContainerProps : Props {
//    var center: LatLng
//    var zoom: Int
//}
//
//val MapContainer = fc<MapContainerProps> { props ->
//    val (map, setMap) = useState<Map?>(null)
//    val markers = useMarkers()
//
//    useEffect(dependencies = emptyList()) {
//        setMap(L.map("map").setView(props.center, props.zoom))
//        return@useEffect { map?.remove() }
//    }
//
//    useEffect(listOf(markers)) {
//        map?.let { map ->
//            markers.forEach { marker -> marker.addTo(map) }
//            map.fitBounds(markers.map { it.getLatLng() })
//        }
//    }
//
//    div {
//        attrs { id = "map" }
//        props.children()
//    }
//}
//
//fun RBuilder.mapContainer(handler: MapContainerProps.() -> Unit) =
//    child(MapContainer) {
//        attrs(handler)
//    }
