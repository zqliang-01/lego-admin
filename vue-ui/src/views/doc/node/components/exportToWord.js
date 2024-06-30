import htmlDocx from 'html-docx-js/dist/html-docx'
import FileSaver from 'file-saver'

/**
 * 导出html为word
 */
export function exportToWord(fileName, html) {
  return new Promise((resolve, reject) => {
    if (!html) {
      reject('导出文档无内容，请编辑后再导出！')
      return
    }
    var imgReg = /<img.*?(?:>|\/>)/gi
    var srcReg = /src=[\'\"\s]?([^\'\"]*)[\'\"\s]?/i
    var matchs = html.match(imgReg)
    var promiseList = []
    if (matchs) {
      for (var i = 0; i < matchs.length; ++i) {
        var match = matchs[i]
        var src = match.match(srcReg)
        if (src && src[1]) {
          const key = 'dataSrc' + i
          promiseList.push(downloadImage(src[1], key))
          html = html.replace(match, `<img style="max-width: 100%; height: auto;" src="${key}" />`)
        }
      }
    }
    html = html.replaceAll('<span class="mce-nbsp-wrap" contenteditable="false">&nbsp;&nbsp;&nbsp;</span><span class="mce-nbsp-wrap" contenteditable="false">&nbsp;&nbsp;&nbsp;</span><span class="mce-nbsp-wrap" contenteditable="false">&nbsp;&nbsp;&nbsp;</span>', '&nbsp;&nbsp;')
    // 将图片转base64------END----------

    Promise.all(promiseList).then(resList => {
      resList.forEach(res => {
        html = html.replace(res.key, res.value)
      })
      const contentHtml = `
            <!DOCTYPE html><html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            </head>
            <body>
                <div>
                    ${html}
                </div>
            </body>
        </html>`
      const converted = htmlDocx.asBlob(contentHtml)
      FileSaver.saveAs(converted, fileName)
      resolve()
    }).catch(res => {
      reject(res)
    })
  })
}

function downloadImage(src, key) {
  return new Promise((resolve, reject) => {
    var image = new Image()
    image.src = src
    image.setAttribute('crossOrigin', '*')
    image.onload = () => {
      var canvas = document.createElement('canvas')
      canvas.width = image.width
      canvas.height = image.height
      var ctx = canvas.getContext('2d')
      ctx.drawImage(image, 0, 0, image.width, image.height)
      resolve({
        key: key,
        value: canvas.toDataURL('image/jpeg')
      })
    }
    image.onerror = () => reject('图片加载失败！')
  })
}
