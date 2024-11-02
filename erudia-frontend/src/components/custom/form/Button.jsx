import { merge } from "../../../utils/merge.js"

const Button = ({ className = "", children, size, color, onClick }) => {
    const classList = merge(className)

    return (
        <button className={classList} type="button" onClick={onClick}>
            {children}
        </button>
    )
}

export { Button }