import { useLocation } from "react-router-dom"

const useSearchParams = (key: string) => {
    return new URLSearchParams(useLocation().search).get(key)
}

export default useSearchParams