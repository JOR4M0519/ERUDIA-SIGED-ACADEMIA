import { merge } from "../../../utils/merge"

const Textarea = ({ className = "", value, size, color, name, id, placeholder ="", onChange }) => {
    const classList = merge(sizeVariants[size], colorVariants[color], "resize-none", className)

    return (
        <textarea
            className={classList}
            value={value}
            name={name}
            id={id}
            placeholder={placeholder}
            onChange={onChange}
        />
    )
}

export { Textarea }