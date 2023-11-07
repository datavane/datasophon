import ajax, { AjaxPromise } from '@ajax';
export class ClusterApi {
    protected $basePath = ''.replace(/\/$/, '');

    public constructor(basePath?: string) {
        if (basePath !== undefined) {
          this.$basePath = basePath.replace(/\/$/, '');
        }
    }

    public frameList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/api/frame/list`;
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

export default new ClusterApi()