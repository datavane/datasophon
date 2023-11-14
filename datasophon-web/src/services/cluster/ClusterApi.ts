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

    public frameDelete (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `api/frame/service/delete/${data.id}`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'GET',
            url,
            ...p
        })
    }

    public saveClusterManager (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/api/cluster/user/saveClusterManager`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public clusterSave (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `api/cluster/save`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public clusterUpdate (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `api/cluster/update`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

}

export default new ClusterApi()