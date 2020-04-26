/*
 * Copyright (c) 2017-2019 The Typelevel Cats-effect Project Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect.internals

import cats.effect.{FiberRef, IO}

private[effect] object IOFiberRef {

  def get[A](ref: FiberRef[A]): IO[Option[A]] =
    IO.Async { (_, ctx, cb) =>
      val k = ref.asInstanceOf[FiberRef[Any]]
      val v = ctx.getLocal(k).map(_.asInstanceOf[A])
      cb(Right(v))
    }

  def set[A](ref: FiberRef[A], value: A): IO[Unit] =
    IO.Async { (_, ctx, cb) =>
      val k = ref.asInstanceOf[FiberRef[Any]]
      val v = value.asInstanceOf[Any]
      ctx.putLocal(k, v)
      cb(Right(()))
    }

}
