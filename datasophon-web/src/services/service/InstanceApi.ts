import ajax, { AjaxPromise } from '@ajax';
export class LoginApi {
    protected $basePath = ''.replace(/\/$/, '');

    public constructor(basePath?: string) {
        if (basePath !== undefined) {
          this.$basePath = basePath.replace(/\/$/, '');
        }
    }

    public getServiceRoleType (form?:any, opt?:any): AjaxPromise<string>  {
      const url = this.$basePath + `/cluster/service/instance/getServiceRoleType`;
      const p: any = {};
      p.form = form;
      return ajax.ajax({
          ...opt,
          method: 'POST',
          url,
          ...p
      })
    }

    public getServiceRoleGroupList (form?:any, opt?:any): AjaxPromise<string>  {
      const url = this.$basePath + `/cluster/service/instance/role/group/list`;
      const p: any = {};
      p.form = form;
      return ajax.ajax({
          ...opt,
          method: 'POST',
          url,
          ...p
      })
    }

    public getLog (form?:any, opt?:any):  AjaxPromise<string>  {
      const url = this.$basePath + `/cluster/service/role/instance/getLog`;
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