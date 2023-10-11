package main

import (
	"fmt"

	"github.com/gin-gonic/gin"
)

func main() {
	// 创建一个默认的路由引擎
	r := gin.Default()
	// GET：请求方式；/hello：请求的路径
	// 当客户端以GET方法请求/hello路径时，会执行后面的匿名函数
	r.GET("/*p", func(c *gin.Context) {
		// c.JSON：返回JSON格式的数据
		pa := c.Param("p")[1:]
		if pa == "" {
			c.String(200, "参数为空!!!")
		} else {
			c.String(200, pa)
		}
	})
	r.POST("/", func(c *gin.Context) {
		// fmt.Println(c.Request.Header)
		// fmt.Println(c.Request.Body)
		// defer c.Request.Body.Close()
		b, _ := c.GetRawData()
		fmt.Println("收到的结果为" + string(b))
	})
	// 启动HTTP服务，默认在0.0.0.0:8080启动服务
	r.Run()
}
