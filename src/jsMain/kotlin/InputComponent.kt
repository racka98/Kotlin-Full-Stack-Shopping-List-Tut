import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.form
import react.useState

external interface InputProps : Props {
    var submit: (String) -> Unit
}

val InputComponent = FC<InputProps> { props ->
    val (text, setText) = useState("")

    form {
        onSubmit = {
            props.submit(text)
            setText("")
        }
        ReactHTML.input {
            css {
                marginTop = 5.px
                marginBottom = 5.px
                fontSize = 14.px
            }
            type = InputType.text
            value = text
            onChange = { event ->
                setText(event.target.value)
            }
        }
    }
}