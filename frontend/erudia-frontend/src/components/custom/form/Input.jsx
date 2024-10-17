import { merge } from "../../../utils/merge"
import { forwardRef } from "react";

const Input = forwardRef(({ className = "", type = "text", value = "", size, color, name, id, required = true, disabled = false, placeholder, onClick, onChange }, ref) => {
    const classList = merge(className, "focus:outline-none")

    return (
        <input
            className={classList}
            type={type}
            value={value}
            name={name}
            id={id}
            ref={ref}
            disabled={disabled}
            required={required}
            placeholder={placeholder}
            onClick={onClick}
            onChange={onChange}
        />
    )
})

export { Input }