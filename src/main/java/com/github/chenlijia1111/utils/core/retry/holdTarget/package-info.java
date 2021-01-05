package com.github.chenlijia1111.utils.core.retry.holdTarget;

/**
 * 关于重试的问题
 * 有些时候业务上调用其他服务，请求失败需要重试，为了方便以后的调用，这里进行封装
 * 场景：调用乙方服务，调用失败，不成功，这个时候可以每隔5分钟后再试
 * 实现参考，微信支付结果通知，成功后通知，如果一直没有应答，会一直通知，但是有最多同荣智次数，且间隔逐渐加长
 *
 * 处理思路：调用方发现调用失败之后，发现调用失败之后，将对应的参数，方法，调用对象存入延时队列
 * 等到了时间再进行调用。
 * 当前处理只需要执行一次就可。是否需要重复处理由调用方决定
 * 循环的时候，放入线程变量中，处理的时候，
 * 判断线程变量中存在这个对象，则表明是循环调用的，这个时候可以自由处理是否要停止重试
 * delay 对象中加上重试次数，调用方可以根据已重试次数来判断是否需要继续重试
 *
 * 调用方需要传递 调用的对象，调用的方法，调用的参数过来。这样不管是否是 spring 环境都没问题了
 *
 * 调用代码如下
 * {@code
 *                  // 判断是否需要进行重试
 *                 if (Objects.nonNull(resultMap) && Objects.equals(1, resultMap.get("errcode"))) {
 *                     // 判断是否需要停止重试，最大重试次数 10，超过 10 次还没有请求成功，就不请求了
 *                     DelayRetryWrapperVo vo = HoldTargetRetryUtil.DELAY_THREAD_LOCAL.get();
 *                     if (Objects.isNull(vo) || vo.getRetryCount() < LIMIT_RETRY_COUNT) {
 *                         // 第一次调用，不是通过重试调用的；或者重试次数小于 5次，需要重试
 *                         if (Objects.isNull(vo)) {
 *                             Object[] args = {groupOrderNo};
 *                             try {
 *                                 Method method = this.getClass().getMethod("noticeAddOrderWithSubmitGroupNo", String.class);
 *                                 vo = new DelayRetryWrapperVo(this,
 *                                         args, method, INIT_RETRY_MINUTES * 60);
 *                             } catch (NoSuchMethodException e) {
 *                                 e.printStackTrace();
 *                             }
 *                         } else {
 *                             // 进入这个判断的，最起码也是第二次循环了
 *                             // 增加访问时间，如：第一次 5分钟后请求，第二次10分钟之后请求,第三次15分钟后请求
 *                             // 依次为 5m 10m 20m 40m 1h10 2h20 4h40 9h20 18h40
 *                             // 设置下次请求的间隔时长 vo.getRetryCount() 为已重试次数，如当前是第二次重试，那么 vo.getRetryCount() 其实是 1
 *                             // 所以下次应该是第三次，用 + 2 表示
 *                             vo.setDelaySeconds(INIT_RETRY_MINUTES * (vo.getRetryCount() + 1) * 60);
 *                         }
 *
 *                         HoldTargetRetryUtil.getInstance().add(vo);
 *                     }
 *
 *
 *                 }
 * }
 *
 */
