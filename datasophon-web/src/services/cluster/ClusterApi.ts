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

    public frameServiceList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/api/frame/service/list`;
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

    public clusterDelete (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `api/cluster/delete`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public clusterServiceList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/service/instance/list`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alarmGroupSave (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/alert/group/save`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alarmGroupDelete (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/alert/group/delete`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alarmGroupList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/alert/group/list`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public getServiceRoleByServiceName (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/api/frame/service/role/getServiceRoleByServiceName`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alertQuotaSave (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/alert/quota/save`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alertQuotaUpdate (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/alert/quota/update`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alertQuotaDelete (data?:any, opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/alert/quota/delete`;
        const p: any = {};
        p.data = data;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alertQuotaStart (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/alert/quota/start`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public alertQuotaStop (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/alert/quota/stop`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public rackList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/rack/list`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public rackSave (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/rack/save`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public rackDelete (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/rack/delete`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public labelSave (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/node/label/save`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public labelList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/node/label/list`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public labelDelete (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/node/label/save`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public userDelete (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/user/delete`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public userCreate (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/user/create`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public groupList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/group/list`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public groupSave(form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/group/save`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public groupDelete (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/group/delete`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public getRoleListByHostname (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/api/cluster/host/getRoleListByHostname`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public generateHostServiceCommand (form?:any , opt?:any): AjaxPromise<string>  {
        console.log('this', this)
        const url = this.$basePath + `/host/install/generateHostServiceCommand`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public generateHostAgentCommand (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/host/install/generateHostAgentCommand`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public reStartDispatcherHostAgent (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/host/install/reStartDispatcherHostAgent`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public labelAssign (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/cluster/node/label/assign`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public assignRack (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `api/cluster/host/assignRack`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public analysisHostList (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/host/install/analysisHostList`;
        const p: any = {};
        p.form = form;
        return ajax.ajax({
            ...opt,
            method: 'POST',
            url,
            ...p
        })
    }

    public rehostCheck (form?:any , opt?:any): AjaxPromise<string>  {
        const url = this.$basePath + `/host/install/rehostCheck`;
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