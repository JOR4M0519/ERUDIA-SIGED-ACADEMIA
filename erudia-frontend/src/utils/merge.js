import clsx from "clsx"
import { twMerge } from "tw-merge"

const merge = (...classes) => {
    return twMerge(clsx(classes.filter(Boolean)))
}

export { merge }