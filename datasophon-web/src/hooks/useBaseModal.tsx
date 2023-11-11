import { useCallback, useContext } from "react"
import { BaseModalContext } from "./baseModalContextProvider";

const modalCallbacks: any = {};
export const useBaseModal = (modalId: string) => {
    const baseContext = useContext(BaseModalContext)
    if(!baseContext) {
        throw new Error("BaseModalContext not found");
    }
    const { state, dispatch } = baseContext
    const args: any = state[modalId]
    const show = useCallback((args: any)=> {
        return new Promise((resolve)=> {
            modalCallbacks[modalId] = resolve;
            dispatch({
                type: 'base-modal/show',
                payload: {
                    modalId,
                    args
                }
            })
        })
    },[modalId, dispatch])

    const resolve = useCallback((args:any) => {
        if (modalCallbacks[modalId]) {
            modalCallbacks[modalId](args)
            delete modalCallbacks[modalId]
        }
    }, [modalId])
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
        visible: !!args,
        resolve
    }
}
