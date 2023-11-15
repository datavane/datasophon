import axios from "axios"
import qs from "qs"

const axiosInstance = axios.create({
    baseURL: '/ddh',
    withCredentials: true,
    timeout: 6000
})

/** 不再兼容非标准的数据结构 */
export declare type AjaxPromise<R> = Promise<R>;
/** 非标准包裹 */
export declare type NonStandardAjaxPromise<R> = Promise<{
  code?: number;
  message?: string;
  result: R;
}>;

export interface ExtraFetchParams {
  /** extra data for extends */
  extra?: any;
  /** 扩展请求头 */
  headers?: any;
  /** cancel request */
  cancel?: Promise<string | undefined>;
}

export interface WrappedFetchParams extends ExtraFetchParams {
    /** http method */
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'OPTIONS' | 'PATCH' | 'HEAD';
    url: string;
    /** post json data */
    data?: any;
    /** post form data */
    form?: any;
    /** query data */
    query?: any;
    /** header */
    header?: any;
    /** path data */
    path?: any;
}

  
export class WrappedFetch {
    /** ajax 方法 */
    public ajax = (
      { method, url, data, form, query, header, extra, headers }: WrappedFetchParams,
      _path?: string,
      _basePath?: string
    ) => {
      let config = {
        ... extra,
        method: method.toLowerCase(),
        headers: { ...headers, ...header },
      };
      // json
      if (data) {
        config = {
          ...config,
          headers: {
            // 可覆盖
            'Content-Type': 'application/json',
            ...config.headers,
          },
          data,
        };
      }
      // form
    if (form) {
        config = {
          ...config,
          headers: {
            // 可覆盖
            'Content-Type': 'application/x-www-form-urlencoded',
            ...config.headers,
          },
          data:
            config.headers && config.headers['Content-Type'] === 'multipart/form-data'
              ? form
              : qs.stringify(form),
        };
      }
      const prom: Promise<unknown> = axiosInstance
      .request({
        ...config,
        url,
        params: query,
      })
      .then((res) => res.data);
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    return prom as Promise<any>;
  };
 /** 接口传参校验 */
 public check<V>(value: V, name: string) {
    if (value === null || value === undefined) {
      const msg = `[ERROR PARAMS]: ${name} can't be null or undefined`;
      // 非生产环境，直接抛出错误
      if (process.env.NODE_ENV === 'development') {
        throw Error(msg);
      }
    }
  }
}

axiosInstance.interceptors.response.use((response) => {
  console.log(response)
  return response
}, ({response: {status}}) => {
  if( status === 401) {
    window.location.replace('/login')
  }
})

export default new WrappedFetch();




