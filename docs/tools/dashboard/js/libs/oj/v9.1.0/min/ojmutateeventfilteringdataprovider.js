/**
 * @license
 * Copyright (c) 2014, 2020, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
define(["ojs/ojcore","jquery","ojs/ojdataprovider","ojs/ojcachediteratorresultsdataprovider","ojs/ojdedupdataprovider","ojs/ojcomponentcore","ojs/ojeventtarget"],function(t,e,a,i,s){"use strict";class r{constructor(t){this.dataProvider=t,this.MutateEventFilteringAsyncIterable=class{constructor(t,e,a,i){this._parent=t,this.params=e,this.dataProviderAsyncIterator=a,this.cache=i,this[Symbol.asyncIterator]=(()=>new this._parent.MutateEventFilteringAsyncIterator(this._parent,this.params,this.dataProviderAsyncIterator,this.cache))}},this.MutateEventFilteringAsyncIterator=class{constructor(t,e,a,i){this._parent=t,this.params=e,this.asyncIterator=a,this.cache=i}next(){let t=this;return this.asyncIterator.next().then(e=>(t._parent.dataProvider instanceof i||t._parent.dataProvider instanceof s||t._parent.cache.addListResult(e),e))}},this.DataProviderMutationEventDetail=class{constructor(t,e,a){this.add=t,this.remove=e,this.update=a,this[r._ADD]=t,this[r._REMOVE]=e,this[r._UPDATE]=a}};let e=this;this.cache=t instanceof i?t.cache:t instanceof s?t.cache:new a.DataCache,t.createOptimizedKeyMap&&(this.createOptimizedKeyMap=(e=>t.createOptimizedKeyMap(e))),t.createOptimizedKeySet&&(this.createOptimizedKeySet=(e=>t.createOptimizedKeySet(e))),t.addEventListener(r._MUTATE,t=>{if(t.detail){let a=e._processMutations(t.detail.remove),i=e._processMutations(t.detail.update);if(a&&a.keys&&a.keys.size>0||i&&i.keys&&i.keys.size>0||t.detail.add&&t.detail.add.keys&&t.detail.add.keys.size>0){let s=new e.DataProviderMutationEventDetail(t.detail.add,a,i),r=Object.assign({},t);r.detail=s,e.dispatchEvent(r)}}else e.dispatchEvent(t)}),t.addEventListener(r._REFRESH,t=>{e.cache.reset(),e.dispatchEvent(t)})}containsKeys(t){return this.dataProvider.containsKeys(t)}fetchByKeys(t){return this.dataProvider.fetchByKeys(t)}fetchByOffset(t){return this.dataProvider.fetchByOffset(t)}fetchFirst(t){const e=this.dataProvider.fetchFirst(t);return new this.MutateEventFilteringAsyncIterable(this,t,e[Symbol.asyncIterator](),this.cache)}getCapability(t){let e=this.dataProvider.getCapability(t);return"eventFiltering"===t?{type:"iterator"}:e}getTotalSize(){return this.dataProvider.getTotalSize()}isEmpty(){return this.dataProvider.isEmpty()}_processMutations(t){if(t){let e=t[r._KEYS];if(e&&e.size>0){let a=new Set,i=this.cache.getDataByKeys({keys:e});e.forEach(function(t){i.results.has(t)||a.add(t)});let s=Object.assign({},t);return a.forEach(function(t){let e=[];s.keys.forEach(function(t){e.push(t)});let a=e.indexOf(t);s.keys.delete(t),delete s.data[a],delete s.indexes[a],delete s.metadata[a]}),s}}return t}}
/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
return r._KEY="key",r._KEYS="keys",r._DATA="data",r._METADATA="metadata",r._ITEMS="items",r._FROM="from",r._OFFSET="offset",r._REFRESH="refresh",r._MUTATE="mutate",r._SIZE="size",r._FETCHPARAMETERS="fetchParameters",r._VALUE="value",r._DONE="done",r._RESULTS="results",r._ADD="add",r._UPDATE="update",r._REMOVE="remove",r._INDEXES="indexes",t.MutateEventFilteringDataProvider=r,t.MutateEventFilteringDataProvider=r,t.EventTargetMixin.applyMixin(r),r});