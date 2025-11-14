# 实现分组的那个 要建立一个中间表 就是 分组id 和scenid的对应关系表（如果没有的话需要弄）


1.depoly一键部署的时候传递了deploy 传递了分组列表a字段：[{id:1,name:分组1，start:1,end:2},{id:2,name:分组2，start:3,end:4}]  

2.后端接收这些数据 需要写个整合的函数 通过b:[{点位信息，weizhi:'xx公里'}] 通过a字段的start和end字段这个区间范围弄到所对应的组里面

3.然后整合成一个新的数组[{id:1,name:分组1，start:1,end:2,weizhi:[{点位信息，weizhi:'xx公里'}]}] 

4.然后返回给前端看之前的getInfo接口怎么返回的，区分一下一键部署和其他的 
