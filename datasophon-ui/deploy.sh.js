const fs = require('fs-extra')
const path = require('path')
const deployTasks = require('./deploy.config.js')

// 将前端产物“搬运”到指定目录
console.log(deployTasks)
const promisedDeployTasks = deployTasks.map((deployTask) => {
  const { clean, source, target, replace } = deployTask
  return new Promise((resolve, reject) => {
    if (!source || !target) {
      return reject(new Error('必须指定 ${source} 和 ${target} !'))
    }
    try {
      const stat = fs.lstatSync(source)
      if (clean === true) {
        // 显式指定clean时，将目标目录清空
        fs.emptyDirSync(path.resolve(__dirname, target))
      }
      if (!stat.isFile()) {
        fs.copySync(
          path.resolve(__dirname, source),
          path.resolve(__dirname, target)
        )
      }
      if (stat.isFile() && replace) {
        let content = fs.readFileSync(source, 'utf-8')
        replace.forEach((item) => {
          content = content.replace(item.pattern, item.replacement)
        })
        fs.outputFileSync(target, content)
      }
      resolve()
    } catch (e) {
      reject(e)
    }
  })
})

async function runTasks() {
  async function runner() {
    for (const task of promisedDeployTasks) {
      await task
    }
  }
  try {
    await runner()
    console.log('部署完成！')
  } catch (e) {
    console.error('部署失败！')
    console.log(e)
  }
}

runTasks()
