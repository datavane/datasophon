import ajax, { AjaxPromise } from '@ajax';
export class LoginApi {
    protected $basePath = ''.replace(/\/$/, '');

    public constructor(basePath?: string) {
        if (basePath !== undefined) {
          this.$basePath = basePath.replace(/\/$/, '');
        }
    }

    public login (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/login`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }
}

export default new LoginApi()