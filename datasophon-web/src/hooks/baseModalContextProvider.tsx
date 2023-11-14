import React, { createContext, useReducer, ReactNode } from "react"

interface BaseModalType {
    [key: string]: boolean;
    hiding?: any;
}

interface ActionType {
    type: string;
    payload: any;
}

interface BaseModalContextType {
    state: BaseModalType;
    dispatch: React.Dispatch<ActionType>
}
export const BaseModalContext = createContext<BaseModalContextType | undefined>(undefined)

export const BaseModalReducer = (state: BaseModalType, action: ActionType) => {
    switch (action.type) {
        case 'base-modal/show':
            return {
                ...state,
                // 这里弹窗的关闭逻辑要重新定义一下
                [action.payload.modalId]: action.payload.args || true,
                hiding: {
                    ...state.hiding,
                    [action.payload.modalId]: false
                }
            }
        case 'base-modal/hide':
            return action.payload.force ? {
                ...state,
                [action.payload.modalId]: false,
                hiding: {
                    [action.payload.modalId]: false,
                }
            }: { ...state, hiding: { [action.payload.modalId]: true, } }
        default:
            return state;
    }
}

type BaseModalContextProviderProps = {
    children: ReactNode
}

export const BaseModalContextProvider: React.FC<BaseModalContextProviderProps> = ({children}) => {
    const [state, dispatch] = useReducer(BaseModalReducer, {} )
    return (
        <BaseModalContext.Provider value={{
            state,
            dispatch
        }}>
            {children}
        </BaseModalContext.Provider>
    )
}