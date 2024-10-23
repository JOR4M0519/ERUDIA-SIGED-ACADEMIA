import { merge } from "../../../utils/merge.js"

const Button = ({ className = "", children, size, color, onClick }) => {
    const classList = merge(sizeVariants[size], colorVariants[color], className)

    return (
        <button className={classList} type="button" onClick={onClick}>
            {children}
        </button>
    )
}

export { Button }