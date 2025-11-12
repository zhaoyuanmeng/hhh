import CustomObject from './customObject.class'

class CustomObjectBIZ extends CustomObject {
    constructor(apiOBJ) {
        super()

        // apiOBJ.addLeftClick(this.leftClick)

        this.customObjectIds = []
        // 电梯的所有objectid
        this.elevatorIds = ['18C14D85B0163EF86C81A10DC7646006+936', '18C14D85B0163EF86C81A10DC7646006+933', '18C14D85B0163EF86C81A10DC7646006+932', '18C14D85B0163EF86C81A10DC7646006+926', '18C14D85B0163EF86C81A10DC7646006+923', '18C14D85B0163EF86C81A10DC7646006+921', '18C14D85B0163EF86C81A10DC7646006+918', '18C14D85B0163EF86C81A10DC7646006+914', '18C14D85B0163EF86C81A10DC7646006+901', '18C14D85B0163EF86C81A10DC7646006+995', '18C14D85B0163EF86C81A10DC7646006+996', '18C14D85B0163EF86C81A10DC7646006+1000', '18C14D85B0163EF86C81A10DC7646006+997', '18C14D85B0163EF86C81A10DC7646006+998', '18C14D85B0163EF86C81A10DC7646006+1001', '18C14D85B0163EF86C81A10DC7646006+1002', '18C14D85B0163EF86C81A10DC7646006+999', '18C14D85B0163EF86C81A10DC7646006+1003']
        this.tileLayerId = '2DE12051461517BAD9E68F8B855E885C' // TODO
        this.leftDoors = '18C14D85B0163EF86C81A10DC7646006+997'
        this.rightDoors = '18C14D85B0163EF86C81A10DC7646006+998'
        this.leftBackDoor = '18C14D85B0163EF86C81A10DC7646006+1001'
        this.rightBackDoor = '18C14D85B0163EF86C81A10DC7646006+1002'
        // 当前所在楼层
        this.customFloor = 1
        // 楼层按钮
        this.floorBtns = ['cusobj18C14D85B0163EF86C81A10DC7646006+932', 'cusobj18C14D85B0163EF86C81A10DC7646006+926', 'cusobj18C14D85B0163EF86C81A10DC7646006+923', 'cusobj18C14D85B0163EF86C81A10DC7646006+921', 'cusobj18C14D85B0163EF86C81A10DC7646006+918', 'cusobj18C14D85B0163EF86C81A10DC7646006+914', 'cusobj18C14D85B0163EF86C81A10DC7646006+901',]
    }
}

// TODO 待优化：电梯场景交互
CustomObjectBIZ.prototype.leftClick = function (data) {
    let that = this
    switch (data.Type) {
        case 'TileLayer':
            // 一层开前门
            if (data.Id === '178c7a945a6f49b58f0cd383f6a197ae') {
                let frondButton = ['18C14D85B0163EF86C81A10DC7646006+948', '18C14D85B0163EF86C81A10DC7646006+949']
                if (frondButton.indexOf(data.ObjectID) > -1) {
                    // 开前门
                    that.getCustomObjects()
                    that.openFrontDoor()
                }
            }
            break
        case 'CustomObj':
            if (!!data.Id && !!that.elevatorIds && that.elevatorIds.indexOf(data.Id.substr(6)) > -1) {
                // 去往某个楼层
                let index = that.floorBtns.indexOf(data.Id)
                if (index > -1) {
                    // 高亮楼层
                    that.highlight(data.Id, Color.Red)
                    that.changePositon(index + 1)
                }
            }
            break
    }
}

CustomObjectBIZ.prototype.getCustomObjects = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (that.customObjectIds.length > 0) resolve()

        await that.clear()

        let objs = []
        that.elevatorIds.map(o => {
            let id = 'cusobj' + o
            that.customObjectIds.push(id)
            let obj = {
                id: id,
                tileLayerId: that.tileLayerId,
                objectId: o,
                coordinateType: 0
            }
            objs.push(obj)
        })
        await that.addByTileLayer(objs)

        await window.sealAPI._tileLayer.hideActors({
            id: that.tileLayerId,
            objectIds: that.elevatorIds
        })
        resolve()
    })
}

/**
 * 开前门
 */
CustomObjectBIZ.prototype.openFrontDoor = function () {
    let that = this
    that.get(that.customObjectIds).then(res => {
        let customArray = res.data
        let interval = setInterval(() => {
            let updateData = []

            customArray.forEach(item => {
                if (that.leftDoors == item.objectId) {
                    item.location[0] -= 0.1
                } else if (that.rightDoors == item.objectId) {
                    item.location[0] += 0.1
                }
                updateData.push({
                    id: item.id,
                    location: item.location
                })
            })

            that.update(updateData)
        }, 100)
        setTimeout(() => {
            clearInterval(interval)
        }, 1000)
        setTimeout(() => {
            that.closeFrontDoor()
        }, 5000)
    })
}

/**
 * 关前门
 */
CustomObjectBIZ.prototype.closeFrontDoor = function () {
    let that = this
    that.get(that.customObjectIds).then(res => {
        let customArray = res.data
        let interval = setInterval(() => {
            let updateData = []

            customArray.forEach(item => {
                if (that.leftDoors == item.objectId) item.location[0] += 0.1
                else if (that.rightDoors == item.objectId) item.location[0] -= 0.1

                updateData.push({
                    id: item.id,
                    location: item.location
                })
            })
            that.update(updateData)
        }, 100)
        setTimeout(() => {
            clearInterval(interval)
        }, 1000)
    })
}

/**
 * 开后门
 */
CustomObjectBIZ.prototype.openBackDoor = function () {
    let that = this
    that.get(that.customObjectIds).then(res => {
        let customArray = res.data
        let interval = setInterval(() => {
            let updateData = []
            customArray.forEach(item => {
                if (that.leftBackDoor == item.objectId) {
                    item.location[0] -= 0.1
                } else if (that.rightBackDoor == item.objectId) {
                    item.location[0] += 0.1
                }
                updateData.push({
                    id: item.id,
                    location: item.location
                })
            })
            that.update(updateData)
        }, 100)
        setTimeout(() => {
            clearInterval(interval)
        }, 1000)
        setTimeout(() => {
            that.closeBackDoor()
        }, 5000)
    })
}

/**
 * 关后门
 */
CustomObjectBIZ.prototype.closeBackDoor = function () {
    let that = this
    that.get(that.customObjectIds).then(res => {
        let customArray = res.data
        let interval = setInterval(() => {
            let updateData = []
            customArray.forEach(item => {
                if (that.leftBackDoor == item.objectId) item.location[0] += 0.1
                else if (that.rightBackDoor == item.objectId) item.location[0] -= 0.1

                updateData.push({
                    id: item.id,
                    location: item.location
                })
            })
            that.update(updateData)
        }, 100)
        setTimeout(() => {
            clearInterval(interval)
        }, 1000)
    })
}

/**
  * 每层高3米，每100ms移动0.3米，
  * 总移动距离为 absDistance*3，
  * absDistance*3/0.3为移动多少次
  * totalTime 为循环的总时间，时间到后结束循环
  */
CustomObjectBIZ.prototype.changePositon = function (floor) {
    let that = this
    let distance = floor - that.customFloor
    let absDistance = Math.abs(distance)
    let height = absDistance * 3
    let heightScale = 0.3
    let intervalTime = 100
    let totalTime = intervalTime * height / heightScale
    that.customFloor = floor
    that.get(that.customObjectIds).then(res => {
        let customArray = res.data
        let interval = setInterval(() => {
            let updateData = []
            customArray.forEach(item => {
                if (distance > 0) item.location[2] += heightScale
                else item.location[2] -= heightScale

                updateData.push({
                    id: item.id,
                    location: item.location
                })
            })
            that.update(updateData)
        }, intervalTime)
        setTimeout(() => {
            clearInterval(interval)
            that.unhighlight()
            that.openBackDoor()
        }, totalTime)
    })
}

CustomObjectBIZ.prototype.delete_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.delete(that.customObjectIds)
        await window.sealAPI._tileLayer.showActors({
            id: that.tileLayerId,
            objectIds: that.elevatorIds
        })
        that.customObjectIds = []
        resolve()
    })
}

export default CustomObjectBIZ