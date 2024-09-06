pdfh = jsp.pdfh
function getToken(html1) {
  let currentId = pdfh(html1, '#current_id&&value')
  let eToken = pdfh(html1, '#e_token&&value')
  if (!currentId || !eToken) return ''
  let idLength = currentId.length
  let subId = currentId.substring(idLength - 4, idLength)
  let keys = []
  for (let i = 0; i < subId.length; i++) {
    let curInt = parseInt(subId[i])

    let splitPos = (curInt % 3) + 1
    keys[i] = eToken.substring(splitPos, splitPos + 8)
    eToken = eToken.substring(splitPos + 8, eToken.length)
  }
  return keys.join('')
}
try {
  VOD = {}
  let html1 = request(input)
  VOD.vod_id = pdfh(html1, '#current_id&&value')
  VOD.vod_name = pdfh(html1, 'h2&&Text')
  VOD.vod_pic = pdfh(html1, '.item-root&&img&&data-src')
  VOD.vod_actor = pdfh(html1, '.meta:eq(4)&&Text')
  VOD.vod_area = pdfh(html1, '.meta:eq(3)&&Text')
  VOD.vod_year = pdfh(html1, '.meta:eq(2)&&Text')
  VOD.vod_remarks = ''
  VOD.vod_director = ''
  VOD.vod_content = pdfh(html1, '#line-tips&&Text')
  // log(VOD);
  var v_tks = getToken(html1)
  log('v_tks ===> ' + v_tks)
  input =
    HOST +
    '/api/getResN?videoId=' +
    input.split('/').pop() +
    '&mtype=2&token=' +
    v_tks
  let html = request(input, {
    headers: {
      'User-Agent':
        'Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1',
      Referer: MY_URL,
    },
  })
  print(html)
  html = JSON.parse(html)
  let episodes = html.data.list
  let playMap = {}
  if (typeof play_url === 'undefined') {
    var play_url = ''
  }
  let map = {}
  let arr = []
  let name = {
    bfzym3u8: '暴风',
    '1080zyk': '优质',
    kuaikan: '快看',
    lzm3u8: '量子',
    ffm3u8: '非凡',
    haiwaikan: '海外看',
    gsm3u8: '光速',
    zuidam3u8: '最大',
    bjm3u8: '八戒',
    snm3u8: '索尼',
    wolong: '卧龙',
    xlm3u8: '新浪',
    yhm3u8: '樱花',
    tkm3u8: '天空',
    jsm3u8: '极速',
    wjm3u8: '无尽',
    sdm3u8: '闪电',
    kcm3u8: '快车',
    jinyingm3u8: '金鹰',
    fsm3u8: '飞速',
    tpm3u8: '淘片',
    lem3u8: '鱼乐',
    dbm3u8: '百度',
    tomm3u8: '番茄',
    ukm3u8: 'U酷',
    ikm3u8: '爱坤',
    hnzym3u8: '红牛资源',
    hnm3u8: '红牛',
    '68zy_m3u8': '68',
    kdm3u8: '酷点',
    bdxm3u8: '北斗星',
    qhm3u8: '奇虎',
    hhm3u8: '豪华',
  }
  episodes.forEach(function (ep) {
    let data = JSON.parse(ep['resData'])
    data.map((val) => {
      if (!map[val.flag]) {
        map[val.flag] = [val.url.replaceAll('##', '#')]
      } else {
        map[val.flag].push(val.url.replaceAll('##', '#'))
      }
    })
  })
  for (var key in map) {
    if ('bfzym3u8' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 1,
      })
    } else if ('1080zyk' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 2,
      })
    } else if ('kuaikan' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 3,
      })
    } else if ('lzm3u8' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 4,
      })
    } else if ('ffm3u8' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 5,
      })
    } else if ('snm3u8' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 6,
      })
    } else if ('qhm3u8' == key) {
      arr.push({
        flag: name[key],
        url: map[key],
        sort: 7,
      })
    } else {
      arr.push({
        flag: name[key] ? name[key] : key,
        url: map[key],
        sort: 8,
      })
    }
  }
  arr.sort((a, b) => a.sort - b.sort)
  let playFrom = []
  let playList = []
  arr.map((val) => {
    if (!/undefined/.test(val.flag)) {
      playFrom.push(val.flag)
      playList.push(val.url)
    }
  })
  let vod_play_from = playFrom.join('$$$')
  let vod_play_url = playList.join('$$$')
  VOD['vod_play_from'] = vod_play_from
  VOD['vod_play_url'] = vod_play_url
  // log(VOD);
} catch (e) {
  log('获取二级详情页发生错误:' + e.message)
}
