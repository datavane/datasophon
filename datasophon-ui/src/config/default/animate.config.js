/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

const direct_s = ['left', 'right']
const direct_1 = ['left', 'right', 'down', 'up']
const direct_1_b = ['downBig', 'upBig', 'leftBig', 'rightBig']
const direct_2 = ['topLeft', 'bottomRight', 'topRight', 'bottomLeft']
const direct_3 = ['downLeft', 'upRight', 'downRight', 'upLeft']

// animate.css 配置
const ANIMATE = {
  preset: [ //预设动画配置
    {name: 'back', alias: '渐近', directions: direct_1},
    {name: 'bounce', alias: '弹跳', directions: direct_1.concat('default')},
    {name: 'fade', alias: '淡化', directions: direct_1.concat(direct_1_b).concat(direct_2).concat('default')},
    {name: 'flip', alias: '翻转', directions: ['x', 'y']},
    {name: 'lightSpeed', alias: '光速', directions: direct_s},
    {name: 'rotate', alias: '旋转', directions: direct_3.concat('default')},
    {name: 'roll', alias: '翻滚', directions: ['default']},
    {name: 'zoom', alias: '缩放', directions: direct_1.concat('default')},
    {name: 'slide', alias: '滑动', directions: direct_1},
  ]
}
module.exports = ANIMATE
