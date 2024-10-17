import { merge } from "../../../utils/merge"

const Form = ({ className = "", children, onSubmit }) => {

    return (
        <form className={merge("w-full flex flex-col gap-y-4", className)} onSubmit={onSubmit}>
            {children}
        </form>
    )
}

export { Form }