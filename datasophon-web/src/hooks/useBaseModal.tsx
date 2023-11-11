import { useCallback, useContext } from "react"
import { BaseModalContext } from "./baseModalContextProvider";

export const useBaseModal = (modalId: string) => {
    const baseContext = useContext(BaseModalContext)
    if(!baseContext) {
        throw new Error("BaseModalContext not found");
    }
    const { state, dispatch } = baseContext
    const args = state[modalId]
    const show = useCallback((args: any)=> {
        dispatch({
            type: 'base-modal/show',
            payload: {
                modalId,
                args
            }
        })
    },[modalId, dispatch])
    const hide = useCallback((force: boolean)=> {
        dispatch({
            type: 'base-modal/hide',
            payload: {
                modalId,
                force
            }
        })
    },[modalId, dispatch])
    return {
        args,
        show,
        hide,
        visible: !!args
    }
}
